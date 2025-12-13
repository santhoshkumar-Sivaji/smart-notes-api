# Phase-1 Technical Architecture – AI-Powered Handwritten Notes Platform

This document defines the **complete Phase-1 technical architecture** for the AI-powered handwritten notes product.

It is based strictly on **previously approved decisions** and follows the **recommended approach**:
- Industry-leading OCR (Google Document AI) in Phase-1
- PostgreSQL everywhere (Docker locally, managed in prod)
- Async-first design
- Strong cost and security boundaries

This architecture is **build-ready**.

---

## 1. Architecture Goals (Phase-1)

- Launch fast without blocking on custom AI
- Keep backend stable while AI evolves
- Ensure zero coin leakage
- Ensure OCR never blocks user actions
- Keep every component replaceable

Why? — Phase-1 is about validation, not perfection.

---

## 2. High-Level Architecture Diagram

```
+-------------------+        HTTPS        +-----------------------+
|                   |  ───────────────▶  |                       |
|    Mobile App     |                    |     Backend API       |
| (iOS / Android)   |  ◀───────────────  |   (Spring Boot)       |
|                   |        JSON        |                       |
+---------+---------+                    +-----------+-----------+
          |                                                |
          | (Image Upload)                                 | (Async Job)
          ▼                                                ▼
+-------------------+                         +----------------------+
|                   |                         |                      |
|  Object Storage   |                         |   Message Queue     |
| (S3 / GCS)        |                         | (RabbitMQ / SQS)    |
|                   |                         |                      |
+-------------------+                         +----------+-----------+
                                                           |
                                                           | (Consume)
                                                           ▼
                                               +----------------------+
                                               |                      |
                                               |     OCR Worker       |
                                               |   (Python Service)  |
                                               |                      |
                                               +----------+-----------+
                                                           |
                                                           | (API Call)
                                                           ▼
                                               +----------------------+
                                               |                      |
                                               | Google Document AI   |
                                               | (Handwriting OCR)   |
                                               |                      |
                                               +----------------------+

+-------------------+        SQL        +---------------------------+
|                   |  ◀────────────▶  |                           |
|   PostgreSQL DB   |                  |   Search Engine           |
| (Docker / Prod)  |                  | (OpenSearch)              |
|                   |                  |                           |
+-------------------+                  +---------------------------+
```

---

## 3. Component Responsibilities

---

## 3.1 Mobile App

**Responsibilities**
- Capture handwritten pages (camera)
- Offline storage of images
- Retry-safe upload
- Trigger OCR explicitly
- Display OCR text and search results

**Key Rules**
- No OCR happens automatically
- Offline-first behavior

Why? — UX speed and cost control.

---

## 3.2 Backend API (Spring Boot)

**Responsibilities**
- Authentication & authorization
- Notebook QR redemption
- Coin ledger enforcement
- Page metadata management
- Async OCR orchestration
- Search API

**Key Rules**
- Never block on OCR
- Coins are always ledger-based
- Backend is the single source of truth

Why? — Stability and correctness.

---

## 3.3 PostgreSQL Database

**Usage**
- Users
- Notebook instances
- Pages
- OCR jobs
- OCR results
- Coin ledger

**Deployment Strategy**
| Environment | DB |
|------------|----|
| Local Dev | PostgreSQL (Docker) |
| Tests | PostgreSQL (Testcontainers) |
| Prod | Managed PostgreSQL |

Why? — Avoids behavioral mismatch and ledger bugs.

---

## 3.4 Object Storage (S3 / GCS)

**Responsibilities**
- Store original page images
- Serve images securely

**Rules**
- Images are immutable
- Access controlled via backend

Why? — Cheap, scalable storage.

---

## 3.5 Message Queue

**Responsibilities**
- Buffer OCR jobs
- Decouple API from OCR processing

**Characteristics**
- At-least-once delivery
- Dead-letter support

Why? — Async reliability.

---

## 3.6 OCR Worker Service

**Responsibilities**
- Consume OCR jobs
- Fetch page images
- Call Google Document AI
- Handle retries & failures
- Return results to backend

**Technology**
- Python service

Why? — AI logic isolated from backend.

---

## 3.7 External OCR (Google Document AI)

**Role**
- Handwriting OCR (Phase-1)

**Why chosen**
- High accuracy
- Fast time-to-market
- No training required

Future replacement:
- Custom TrOCR / Donut models

---

## 3.8 Search Engine (Keyword Only)

**Responsibilities**
- Index OCR text
- Support keyword search

**Technology**
- OpenSearch / Elasticsearch

Why? — Simple discovery in MVP.

---

## 4. End-to-End Flow (Phase-1)

### Page OCR Flow
1. User uploads page image
2. Backend stores metadata + image
3. User requests OCR
4. Backend reserves coin
5. OCR job enqueued
6. Worker processes OCR
7. Result stored & indexed
8. Coin debited

Why? — Safe, async, cost-controlled.

---

## 5. Security Boundaries

- Mobile → Backend: HTTPS + Auth
- Backend → Storage: Private access
- Backend → Queue: Authenticated
- Worker → Backend: Internal auth
- Backend → DocAI: API keys

Why? — Zero trust.

---

## 6. Replaceability & Evolution

| Component | Phase-1 | Phase-2+ |
|---------|--------|---------|
| OCR | Document AI | Custom OCR |
| Search | Keyword | Semantic |
| DB | PostgreSQL | PostgreSQL |
| Queue | Managed | Managed |

Why? — Architecture survives change.

---

## 7. Explicit Phase-1 Constraints

- No AI summaries
- No personalization
- No semantic search
- No offline OCR

Why? — Scope discipline.

---

## 8. Phase-1 Success Criteria

Architecture is successful if:
- Backend APIs stay fast
- OCR failures never block users
- Coins are never leaked
- Components can be swapped later

---

## 9. What This Architecture Enables Next

- Smooth Phase-2 AI expansion
- Cost optimisation via custom OCR
- Personalised handwriting models

---

*Phase-1 Technical Architecture locked.*

