# Security & Rate-Limiting – Phase 1 Design

This document defines **security, abuse-prevention, and rate-limiting controls** for Phase‑1 of the AI-powered handwritten notes platform.

Scope is strictly aligned with:
- Phase‑1 User Stories (US‑2 to US‑6)
- Phase‑1 Backend APIs
- Phase‑1 Database & Async Job design

Goal: **Protect user data, prevent fraud, and control AI cost** without hurting UX.

---

## 1. Security Principles (Phase‑1)

- Zero trust between clients and backend
- Backend is the single source of truth
- Coins are treated as money-like assets
- AI calls must be explicitly gated
- Fail safely (deny by default)

Why? — AI misuse and coin abuse are existential risks.

---

## 2. Authentication & Session Security

### 2.1 Authentication Model

**Required**:
- Token-based authentication (JWT / OAuth access token)
- Token bound to user identity

**Not Required (Phase‑1)**:
- Multi-factor authentication
- Role-based access control

Why? — Single-user accounts only in Phase‑1.

---

### 2.2 Token Rules

- Short-lived access tokens
- Refresh tokens (if used) stored securely
- Token must be sent on every API call

Backend must:
- Reject expired tokens
- Reject tampered tokens

Why? — Prevents replay and session hijack.

---

## 3. Authorization Rules

All API access must validate:
- User owns the resource (page, notebook, job)
- PageId / JobId always scoped to authenticated user

Examples:
- User cannot OCR another user’s page
- User cannot search another user’s notes

Why? — Prevents data leaks.

---

## 4. QR Code Security

### 4.1 QR Code Structure

QR must include:
- Notebook SKU ID
- Unique nonce
- Expiry (optional)
- Cryptographic signature (server‑verifiable)

QR data must never be guessable.

---

### 4.2 QR Redemption Rules

Backend must:
- Verify signature
- Verify QR not already redeemed
- Reject reused or tampered QR

Why? — Prevents free coin generation.

---

## 5. Upload Security

### 5.1 File Validation

Backend must validate:
- File type (image only)
- File size limit
- Image decode safety

Reject:
- Executables
- Oversized files

Why? — Prevents storage and malware abuse.

---

### 5.2 Upload Rate Limits

Limits (per user):
- Max uploads per minute
- Max total pages per day (soft limit)

Why? — Prevents storage abuse.

---

## 6. OCR & AI Abuse Prevention

### 6.1 Explicit User Intent

OCR must be:
- Triggered only via explicit API call
- Never automatic on upload

Why? — Cost control.

---

### 6.2 Coin Gating Rules

Backend must:
- Check balance before OCR job creation
- Reserve coins before enqueue
- Refund on failure

No coin → no OCR job.

Why? — Hard cost boundary.

---

### 6.3 OCR Rate Limits

Limits (per user):
- Max OCR requests per hour
- Max concurrent OCR jobs

Why? — Prevents queue flooding and runaway cost.

---

## 7. API Rate Limiting (Global)

### 7.1 Rate Limit Dimensions

Rate limits applied by:
- User ID
- IP address (secondary)

---

### 7.2 Suggested Limits (Phase‑1)

| API Category | Limit |
|------------|-------|
| Auth | Low (protect brute force) |
| Upload | Moderate |
| OCR Request | Strict |
| Search | Moderate |

Why? — Different APIs have different cost profiles.

---

## 8. Idempotency & Replay Protection

- client_page_id required for uploads
- OCR request idempotent per page
- Duplicate requests return existing job

Why? — Mobile retry safety.

---

## 9. Data Protection & Privacy

### 9.1 Data at Rest

- Encrypt images and sensitive fields
- Secure object storage buckets

### 9.2 Data in Transit

- HTTPS only
- Secure internal service calls

Why? — User trust.

---

## 10. Logging, Audit & Alerting

### 10.1 Audit Logs

Must log:
- QR redemption
- Coin ledger entries
- OCR job creation
- OCR failures

Why? — Investigations and dispute resolution.

---

### 10.2 Alerts

Alert on:
- Spike in OCR failures
- Spike in OCR volume
- QR abuse attempts
- Coin ledger anomalies

Why? — Early detection of abuse.

---

## 11. Phase‑1 Explicit Exclusions

Security design does NOT include:
- Advanced fraud ML
- User-to-user sharing permissions
- Enterprise IAM

Why? — MVP scope discipline.

---

## 12. Phase‑1 Success Criteria

Security & rate-limiting is successful if:
- No unauthorized data access
- No free OCR usage
- No coin leakage
- AI costs remain predictable

---

## 13. Next Steps

After this document:
1. OCR Provider Integration Design
2. Deployment & environment hardening
3. Implementation & testing

---

*Security & Rate-Limiting – Phase‑1 locked.*

