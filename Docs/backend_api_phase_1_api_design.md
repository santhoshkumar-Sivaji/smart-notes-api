# Backend API – Phase 1 API Design

This document defines the **Phase‑1 Backend API contracts** for the AI‑powered handwritten notes product.

Scope is **strictly limited to Phase‑1 user stories (US‑2 to US‑6)** and the previously approved **Phase‑1 Backend Requirements**.

No Phase‑2 or Phase‑3 features are included.

---

## 1. API Design Principles (Phase‑1)

- APIs are **behavior‑driven**, not data‑driven  
- OCR and AI processing is **always asynchronous**  
- Backend is the **single source of truth**  
- Coins are treated as **money‑like value** (ledger based)  
- All APIs must be **idempotent** where retries are possible

Why? — Prevents data corruption, cost leakage, and refactors.

---

## 2. Authentication & Context

All APIs assume:
- User is authenticated
- User identity is resolved before request processing

Auth mechanism (JWT / OAuth) is intentionally abstracted in this document.

---

## 3. API Groups Overview

| Domain | APIs |
|------|-----|
| Notebook & QR | Redeem notebook QR |
| Pages & Upload | Upload page image, list pages |
| OCR Orchestration | Request OCR, get OCR status |
| Search | Keyword search |
| Coins | Get coin balance |

---

## 4. Notebook & QR APIs

### 4.1 Redeem Notebook QR

**Purpose**  
Redeem a physical notebook and credit coins to the user.

**Endpoint**  
`POST /api/v1/notebooks/redeem`

**Request**
```json
{
  "qrPayload": "<signed-qr-data>"
}
```

**Response (Success)**
```json
{
  "notebookInstanceId": "nb_123",
  "coinsCredited": 30,
  "currentBalance": 30
}
```

**Failure Scenarios**
- QR already redeemed
- Invalid or tampered QR
- Expired QR

**Notes**
- Redemption must be **atomic**
- QR can be redeemed **only once**

Why? — Prevents fraud and double credit.

---

## 5. Page Upload & Storage APIs

### 5.1 Upload Page Image

**Purpose**  
Upload a handwritten page image and create a page record.

**Endpoint**  
`POST /api/v1/pages`

**Request**  
Multipart form:
- imageFile
- notebookInstanceId (optional)
- clientPageId (for idempotency)

**Response**
```json
{
  "pageId": "pg_456",
  "status": "STORED"
}
```

**Notes**
- Must support retries safely
- Original image must be preserved

Why? — Offline sync depends on idempotency.

---

### 5.2 List Pages

**Purpose**  
Retrieve user pages for browsing.

**Endpoint**  
`GET /api/v1/pages`

**Response**
```json
[
  {
    "pageId": "pg_456",
    "createdAt": "2025-01-01T10:00:00Z",
    "ocrStatus": "NOT_REQUESTED"
  }
]
```

---

## 6. OCR Orchestration APIs

### 6.1 Request OCR Processing

**Purpose**  
Trigger handwriting OCR for a page using coins.

**Endpoint**  
`POST /api/v1/pages/{pageId}/ocr`

**Request**
```json
{
  "mode": "BASIC"
}
```

**Response**
```json
{
  "jobId": "job_789",
  "status": "QUEUED"
}
```

**Rules**
- Coin availability must be checked
- Coin is **reserved**, not yet deducted

Why? — OCR is async and billable.

---

### 6.2 Get OCR Status

**Purpose**  
Check OCR job status and retrieve result.

**Endpoint**  
`GET /api/v1/ocr/jobs/{jobId}`

**Response (Processing)**
```json
{
  "status": "IN_PROGRESS"
}
```

**Response (Completed)**
```json
{
  "status": "COMPLETED",
  "extractedText": "Meeting notes ..."
}
```

**Failure Handling**
- On failure → coin reservation is reversed

---

## 7. Search APIs (Phase‑1)

### 7.1 Keyword Search

**Purpose**  
Search handwritten notes by extracted text.

**Endpoint**  
`GET /api/v1/search`

**Query Params**
- q (keyword)

**Response**
```json
[
  {
    "pageId": "pg_456",
    "snippet": "meeting discussion..."
  }
]
```

Why? — Core retrieval capability.

---

## 8. Coin APIs

### 8.1 Get Coin Balance

**Purpose**  
Show user current coin balance.

**Endpoint**  
`GET /api/v1/coins/balance`

**Response**
```json
{
  "balance": 28
}
```

---

## 9. Error & Idempotency Rules

- Upload APIs must support retries
- OCR requests must be idempotent per page
- Coin operations must be atomic
- All failures must return clear error codes

Why? — Mobile networks are unreliable.

---

## 10. Explicit Phase‑1 Exclusions

The following are intentionally excluded:
- AI summaries
- Task extraction
- Semantic search
- Personalisation
- Subscriptions
- Webhooks

---

## 11. Phase‑1 API Success Criteria

APIs are considered successful if:
- No duplicate uploads
- No double coin deduction
- OCR jobs never block API responses
- Search reliably returns OCRed pages

---

## 12. Next Steps

After approval of this document:
1. Database schema design
2. Async job & queue design
3. Security & rate‑limit rules

---

*Phase‑1 API contracts locked.*

