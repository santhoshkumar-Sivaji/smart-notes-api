# OCR Provider Integration – Phase 1 (Google Document AI)

This document defines **how Phase-1 integrates with Google Document AI** for handwritten OCR.

Scope is **strictly Phase-1** and aligned with:
- Phase-1 Technical Architecture
- Phase-1 API, DB, Async, and Security designs

This integration is designed to be **replaceable** by a custom OCR engine in later phases.

---

## 1. Why Google Document AI for Phase-1

Google Document AI is chosen because it:
- Has strong handwriting recognition accuracy
- Handles noisy mobile scans well
- Requires zero model training
- Provides structured OCR output

Why? — Phase-1 goal is **accuracy + speed to market**, not model ownership.

---

## 2. Processor Selection

**Recommended Processor**:
- *Document OCR / Handwriting Processor* (latest version)

Capabilities used in Phase-1:
- Line-level text extraction
- Bounding boxes (optional, stored but not exposed)

Capabilities NOT used in Phase-1:
- Entity extraction
- Form parsing
- Table extraction

Why? — Keep cost and complexity low.

---

## 3. Integration Architecture

```
OCR Worker
   │
   │ (Upload image bytes)
   ▼
Google Document AI
   │
   │ (OCR response)
   ▼
OCR Worker
   │
   │ (normalized text)
   ▼
Backend API → DB → Search Index
```

Why? — Backend remains orchestration-only.

---

## 4. Input Preparation

### 4.1 Image Requirements

Before sending to DocAI:
- Convert to JPEG or PNG
- Max resolution capped (e.g. 300 DPI equivalent)
- Rotate to upright orientation
- Basic contrast enhancement (optional)

Why? — Improves OCR accuracy and reduces cost.

---

## 5. Request Structure (Logical)

OCR Worker sends:
- Image bytes
- MIME type
- Processor ID

No user data or metadata is sent.

Why? — Data minimization and privacy.

---

## 6. Response Handling

### 6.1 Data Extracted

From DocAI response:
- Full text
- Page-level text blocks
- Confidence scores (if available)

Stored in Phase-1:
- Full extracted text only

Why? — Keyword search needs only text.

---

### 6.2 Normalization

OCR Worker must:
- Preserve line breaks
- Normalize whitespace
- Remove obvious OCR artifacts

Why? — Search quality.

---

## 7. Error Handling & Mapping

### 7.1 Error Categories

| Error Type | Action |
|----------|-------|
| Image unreadable | Fail job, refund coin |
| API timeout | Retry |
| API quota exceeded | Fail job, alert |
| Invalid response | Fail job |

Why? — User must never lose coins unfairly.

---

## 8. Retry Strategy

- Max retries per job: 3
- Exponential backoff
- After max retries → FAILED

Why? — Prevent infinite cost loops.

---

## 9. Cost Model (Phase-1)

Typical cost assumptions:
- Per handwritten page OCR: **~$0.05 – $0.20**

Controls:
- OCR only on explicit request
- Coin-based gating
- Rate limits enforced upstream

Why? — Predictable spend.

---

## 10. Security Considerations

- DocAI API keys stored securely
- OCR Worker uses restricted service account
- No direct client access to DocAI

Why? — Prevent API key leakage.

---

## 11. Observability

Metrics to capture:
- OCR latency
- Success vs failure rate
- Cost per page
- Retry count

Why? — Operational visibility.

---

## 12. Replaceability Strategy

To replace DocAI later:
- Keep OCR Worker interface stable
- Swap internal OCR call
- Preserve backend contracts

Why? — Smooth migration to custom OCR.

---

## 13. Phase-1 Explicit Exclusions

This integration does NOT include:
- OCR personalization
- Multi-language tuning
- Layout intelligence
- AI summarization

Why? — MVP scope control.

---

## 14. Phase-1 Success Criteria

Integration is successful if:
- OCR accuracy is acceptable for majority of users
- OCR failures do not block user flow
- Coins are never deducted incorrectly
- OCR cost remains predictable

---

## 15. Next Steps

After this document:
- Final implementation plan
- Development start
- Phase-1 MVP build

---

*OCR Provider Integration – Phase-1 locked.*

