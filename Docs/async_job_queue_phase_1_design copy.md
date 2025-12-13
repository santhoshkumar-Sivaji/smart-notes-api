# Async Job & Queue – Phase 1 Design

This document defines the **asynchronous job and queue design** for Phase‑1 of the AI‑powered handwritten notes platform.

It focuses on **OCR processing**, **coin safety**, and **system reliability**.

Derived strictly from:
- Phase‑1 User Stories (US‑2 to US‑6)
- Phase‑1 Backend Requirements
- Phase‑1 API Design
- Phase‑1 Database Design

---

## 1. Why Async Processing Is Mandatory

Handwriting OCR is:
- Slow (seconds, not milliseconds)
- Costly (paid external APIs)
- Failure‑prone (network, model, image quality)

Therefore:
- **No API call should wait for OCR completion**
- All OCR must run asynchronously

Why? — Keeps backend fast, scalable, and cost‑safe.

---

## 2. High‑Level Architecture

```
Mobile App
   │
   │  (Request OCR)
   ▼
Backend API
   │
   │  (enqueue job)
   ▼
Message Queue  ──►  OCR Worker Service  ──►  External OCR (DocAI)
   ▲                      │
   │                      ▼
   └────────── Backend Callback / Poll ──────┘
```

Why? — Decouples user requests from long‑running work.

---

## 3. Queue Technology (Abstract)

Any of the following are suitable:
- RabbitMQ
- Kafka
- AWS SQS
- GCP Pub/Sub

**Design is queue‑agnostic**.

Why? — Infrastructure flexibility.

---

## 4. Job Types (Phase‑1)

### 4.1 OCR_JOB

**Purpose**: Perform handwriting OCR for a page.

**Triggered by**:
- `POST /pages/{pageId}/ocr`

**Consumes**:
- 1 coin (reserved first)

Why? — Core async workload.

---

## 5. Job Lifecycle & States

| State | Meaning |
|-----|--------|
| QUEUED | Job created, waiting for worker |
| IN_PROGRESS | Worker picked up job |
| COMPLETED | OCR succeeded |
| FAILED | OCR failed |

Why? — Clear visibility and retry control.

---

## 6. Detailed OCR Job Flow

### Step 1: Job Creation (Backend API)

- Validate user owns page
- Check coin balance
- Create **RESERVE** entry in `coin_ledger`
- Create `ocr_jobs` record (status = QUEUED)
- Publish message to queue

Why? — Guarantees cost control before work starts.

---

### Step 2: Job Execution (OCR Worker)

Worker actions:
- Fetch job details
- Download page image
- Call external OCR API (Document AI)
- Handle timeouts & retries

Why? — Isolates failures from main backend.

---

### Step 3: Success Handling

On OCR success:
- Store extracted text in `ocr_results`
- Update `ocr_jobs.status = COMPLETED`
- Convert coin **RESERVE → DEBIT**
- Index text for search

Why? — Atomic completion.

---

### Step 4: Failure Handling

On OCR failure:
- Update `ocr_jobs.status = FAILED`
- Create **REFUND** ledger entry
- Capture failure reason

Why? — User must never lose coins on failure.

---

## 7. Retry Strategy

### Worker Retries
- Retry OCR call up to N times (e.g. 3)
- Exponential backoff

### Job Retries
- Failed jobs are **not auto‑retried indefinitely**
- User must explicitly re‑trigger OCR

Why? — Prevents runaway cost.

---

## 8. Idempotency Rules

- One active OCR job per page at a time
- Duplicate OCR requests return existing job
- Job messages include unique jobId

Why? — Mobile clients may retry.

---

## 9. Timeout & Dead‑Letter Handling

- Jobs exceeding max time → FAILED
- Failed jobs can be sent to Dead‑Letter Queue (DLQ)

Why? — Operational visibility.

---

## 10. Observability & Monitoring

Metrics to track:
- Jobs queued / completed / failed
- OCR latency
- Cost per page
- Refund rate

Alerts:
- High failure rate
- Queue backlog

Why? — AI costs must be visible.

---

## 11. Phase‑1 Explicit Exclusions

Async system will NOT handle:
- AI summaries
- Multi‑stage pipelines
- Model training jobs
- Personalisation learning

Why? — Phase‑1 scope discipline.

---

## 12. Phase‑1 Success Criteria

Async system is successful if:
- Backend APIs stay fast
- OCR never blocks users
- Coins are never lost incorrectly
- Failures are visible and recoverable

---

## 13. Next Steps

After this design:
1. Security & rate‑limiting rules
2. OCR provider integration details
3. Deployment & scaling strategy

---

*Async Job & Queue Design – Phase‑1 locked.*

