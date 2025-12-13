# Backend Database – Phase 1 Design

This document defines the **Phase-1 database design** for the AI-powered handwritten notes platform.

It is derived **strictly from**:
- Phase-1 User Stories (US-2 to US-6)
- Phase-1 Backend Requirements
- Phase-1 API Design

Scope is intentionally limited to **MVP / Launch Phase**.

---

## 1. Database Design Principles (Phase-1)

- Relational database as the source of truth
- Strong consistency for coins and entitlements
- Append-only ledger for money-like operations
- Simple schema, no premature optimisation
- All tables owned by backend (no AI ownership)

Why? — Prevents data corruption and simplifies evolution.

---

## 2. Core Entities Overview

| Entity | Purpose |
|------|---------|
| users | Identify account owner |
| notebook_sku | Define notebook product |
| notebook_instance | Represent a physical notebook |
| pages | Represent scanned handwritten pages |
| ocr_jobs | Track OCR processing |
| ocr_results | Store extracted text |
| coin_ledger | Track coin credits/debits |

---

## 3. Table Definitions (Logical)

### 3.1 users

**Purpose**: Root identity for all data ownership.

**Columns**:
- id (PK)
- created_at

Why? — Every object belongs to a user.

---

### 3.2 notebook_sku

**Purpose**: Define notebook type and entitlement size.

**Columns**:
- id (PK)
- name
- page_count
- created_at

Why? — Allows multiple notebook products later.

---

### 3.3 notebook_instance

**Purpose**: Represent one physical notebook owned by a user.

**Columns**:
- id (PK)
- sku_id (FK → notebook_sku.id)
- user_id (FK → users.id)
- qr_hash (unique)
- redeemed_at

Constraints:
- qr_hash must be unique

Why? — Prevents QR reuse.

---

### 3.4 pages

**Purpose**: Represent a scanned handwritten page.

**Columns**:
- id (PK)
- user_id (FK → users.id)
- notebook_instance_id (FK → notebook_instance.id, nullable)
- image_url
- client_page_id (nullable)
- created_at

Indexes:
- (user_id, created_at)
- (user_id, client_page_id) unique

Why? — Atomic unit of notes.

---

### 3.5 ocr_jobs

**Purpose**: Track OCR processing lifecycle.

**Columns**:
- id (PK)
- page_id (FK → pages.id)
- user_id (FK → users.id)
- status (QUEUED, IN_PROGRESS, COMPLETED, FAILED)
- reserved_coins
- created_at
- updated_at

Indexes:
- (page_id)
- (status)

Why? — Async OCR orchestration.

---

### 3.6 ocr_results

**Purpose**: Store extracted searchable text.

**Columns**:
- id (PK)
- page_id (FK → pages.id, unique)
- extracted_text
- created_at

Why? — Search depends on this.

---

### 3.7 coin_ledger

**Purpose**: Append-only ledger for coins.

**Columns**:
- id (PK)
- user_id (FK → users.id)
- transaction_type (CREDIT, DEBIT, REFUND, RESERVE)
- amount
- reason
- reference_id (nullable)
- created_at

Indexes:
- (user_id, created_at)

Rules:
- No UPDATE or DELETE allowed

Why? — Money-like safety.

---

## 4. Derived Fields (Computed, Not Stored)

- Coin balance = SUM(coin_ledger.amount)
- Page OCR status = derived from ocr_jobs

Why? — Prevents inconsistencies.

---

## 5. Relationships Summary

- User → many notebook_instance
- Notebook_sku → many notebook_instance
- Notebook_instance → many pages
- Page → zero or one ocr_job
- Page → zero or one ocr_result
- User → many coin_ledger entries

---

## 6. Idempotency & Consistency Rules

- client_page_id ensures safe mobile retries
- qr_hash uniqueness prevents duplicate redemption
- coin operations always ledger-based

Why? — Mobile networks are unreliable.

---

## 7. Phase-1 Explicit Exclusions

Database will NOT include:
- AI summaries
- Task tables
- Vector embeddings
- Personalisation profiles
- Subscription plans

Why? — Scope control.

---

## 8. Phase-1 Success Criteria

Database design is successful if:
- No duplicate pages exist
- No QR can be redeemed twice
- Coin balance always reconciles
- OCR results always map to original image

---

## 9. Next Steps

After this design is approved:
1. Async job & queue design
2. Security & access control rules
3. OCR provider integration mapping

---

*Phase-1 Database Design locked.*

