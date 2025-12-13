# Backend API – Phase 1 Requirements (Derived from User Stories)

This document defines **Backend API responsibilities and requirements** derived **strictly from Phase‑1 user stories (US‑1 to US‑6)**.

Scope is intentionally limited to **MVP / Launch Phase**.
No future features are assumed.

---

## 1. Purpose of the Backend in Phase 1

The backend in Phase 1 exists to:
- Safely store user data
- Bridge mobile app ↔ storage ↔ OCR services
- Enforce QR-based entitlement (coins)
- Provide reliable search over handwritten notes

The backend **does NOT**:
- Perform OCR or AI logic itself
- Block requests waiting for AI
- Implement personalization or learning

Why? — Keeps MVP simple, scalable, and cost‑controlled.

---

## 2. Phase‑1 User Stories Covered

| User Story | Description |
|-----------|------------|
| US‑1 | Write on physical notebook |
| US‑2 | Scan handwritten pages |
| US‑3 | Store notes digitally |
| US‑4 | Convert handwriting to searchable text |
| US‑5 | Search notes |
| US‑6 | Redeem notebook QR code |

Only backend behavior needed to support these is included.

---

## 3. Core Backend Responsibilities (Phase 1)

### 3.1 User & Identity Management

**Backend must:**
- Identify users uniquely
- Associate all notebooks, pages, and coins with a user

**Backend must NOT:**
- Enforce complex roles or permissions

Why? — Phase 1 assumes a single owner per account.

---

### 3.2 Notebook & QR Redemption

**Backend must:**
- Validate QR code authenticity
- Prevent QR reuse
- Create a digital notebook instance
- Credit coins based on notebook page count

**Backend must guarantee:**
- One QR → one redemption
- Coins are granted only once

**Backend must NOT:**
- Allow offline coin redemption

Why? — Prevent fraud and cost leakage.

---

### 3.3 Coin & Entitlement Handling

**Backend must:**
- Maintain a coin balance per user
- Record all coin credits and debits in a ledger
- Support coin reservation for OCR jobs

**Backend must guarantee:**
- Coins are deducted only after successful OCR
- Coins are refunded on failure

**Backend must NOT:**
- Directly update coin balance without ledger entry

Why? — Coins represent money‑like value.

---

### 3.4 Page Upload & Storage

**Backend must:**
- Accept page image uploads
- Store original image without modification
- Associate page with a user and notebook
- Support offline upload synchronization

**Backend must guarantee:**
- No data loss after successful upload
- Idempotent uploads (retry safe)

Why? — Users must trust that notes are never lost.

---

### 3.5 OCR Orchestration (Phase‑1)

**Backend must:**
- Trigger OCR only on explicit user request
- Enqueue OCR jobs asynchronously
- Track OCR job status
- Store extracted text

**Backend must guarantee:**
- OCR never blocks API responses
- OCR result always links to original image

**Backend must NOT:**
- Perform OCR itself

Why? — OCR is expensive and slow; backend orchestrates only.

---

### 3.6 Search (Keyword Only)

**Backend must:**
- Index extracted text
- Support keyword‑based search
- Return references to original pages

**Backend must NOT:**
- Perform semantic search (Phase‑2)

Why? — Keyword search is sufficient for MVP.

---

## 4. Core Domain Objects (Conceptual)

### 4.1 User
- userId
- createdAt

Why? — Root identity.

---

### 4.2 NotebookSKU
- skuId
- pageCount

Why? — Defines entitlement size.

---

### 4.3 NotebookInstance
- notebookInstanceId
- skuId
- userId
- redeemedAt

Why? — Represents one physical notebook.

---

### 4.4 Page
- pageId
- notebookInstanceId
- userId
- imageUrl
- createdAt

Why? — Atomic unit of notes.

---

### 4.5 OCRResult
- pageId
- extractedText
- status

Why? — Makes notes searchable.

---

### 4.6 CoinLedger
- transactionId
- userId
- type (CREDIT / DEBIT / REFUND)
- amount
- reason
- createdAt

Why? — Auditable money‑like system.

---

## 5. Required Backend Workflows

### 5.1 Page Upload Flow
1. Mobile uploads image
2. Backend stores image
3. Page record created
4. Page status = STORED

Why? — Simple and reliable capture.

---

### 5.2 QR Redemption Flow
1. User scans QR
2. Backend validates signature
3. NotebookInstance created
4. Coin credit ledger entry created

Why? — Secure entitlement.

---

### 5.3 OCR Flow (Phase‑1)
1. User requests OCR
2. Backend reserves coin
3. OCR job enqueued
4. OCR service processes
5. Backend stores result
6. Coin debit confirmed

On failure:
- Coin reservation reversed

Why? — Cost safety and user trust.

---

### 5.4 Search Flow
1. User submits keyword
2. Backend queries text index
3. Results mapped to pages
4. Page image + text returned

Why? — Simple discovery.

---

## 6. Non‑Functional Requirements (Phase 1)

- API latency < 200 ms (non‑OCR calls)
- OCR always async
- Uploads retry‑safe
- Coin ledger strongly consistent
- Basic monitoring & logging

Why? — MVP must be stable even if minimal.

---

## 7. Explicit Phase‑1 Exclusions

Backend will NOT handle:
- AI summaries
- Task extraction
- Semantic search
- Personalization
- Subscriptions
- On‑device AI

Why? — Scope control and faster launch.

---

## 8. Success Criteria (Backend)

Backend Phase‑1 is successful if:
- No lost pages
- No double coin spend
- QR fraud impossible
- OCR failures do not charge users
- Search reliably finds notes

---

## 9. Next Step

Once this document is approved, the next steps are:
1. Define **API contracts** (endpoints & payloads)
2. Define **database schema**
3. Define **async job & queue design**

This document intentionally stops before implementation.

---

*Phase‑1 Backend Requirements locked.*

