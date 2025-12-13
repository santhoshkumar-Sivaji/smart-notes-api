# User Stories – AI-Powered Handwritten Notes Product

This document captures **all user stories** for the AI-powered handwritten notes ecosystem.

Purpose of this document:
- Define **what the product does from a user perspective**
- Act as the **single source of truth** for product behavior
- Drive backend, mobile, and AI design decisions
- Control scope by clearly separating phases

This document is intentionally **non-technical**.

---

## 1. Product Context

Users prefer writing on physical paper because it is natural, distraction-free, and expressive. However, handwritten notes are difficult to store, search, and reuse long term.

This product bridges that gap by combining:
- **Premium physical notebooks**
- **A mobile app for capture**
- **AI-powered OCR and intelligence**

Users write freely on paper and selectively digitize what matters.

---

## 2. Phase 1 – Capture & Remember (MVP)

**Goal:** Launch quickly and prove value with minimal AI.

---

### US-1: Write on Physical Notebook
**As a user**, I want to write my notes freely on a premium paper notebook, so that I can think clearly without digital distractions.

**Acceptance Criteria**
- Writing experience is comfortable and premium
- No app interaction is required while writing

---

### US-2: Scan Handwritten Pages
**As a user**, I want to take a quick photo of my handwritten page using the app, so that I can store it digitally.

**Acceptance Criteria**
- Scan works even without internet
- Image is stored locally when offline
- User can sync later when online

---

### US-3: Store Notes Digitally
**As a user**, I want my scanned pages to be stored safely in the app, so that I don’t lose important notes.

**Acceptance Criteria**
- Pages persist across app restarts
- Pages are associated with my account
- Original images are never altered or lost

---

### US-4: Convert Handwriting to Searchable Text
**As a user**, I want my handwritten notes converted into searchable text, so that I can find them later.

**Acceptance Criteria**
- Text extraction is explicitly triggered by user
- OCR result is linked to original page image
- OCR text can be searched

---

### US-5: Search My Notes
**As a user**, I want to search my notes using keywords, so that I can quickly find past information.

**Acceptance Criteria**
- Search returns relevant pages
- Selecting a result opens the original page

---

### US-6: Redeem Notebook QR Code
**As a user**, I want to scan a QR code from my notebook, so that I receive credits to process my handwritten pages.

**Acceptance Criteria**
- QR code can be redeemed only once
- Credits match notebook page count
- Credit balance is clearly visible

---

## 3. Phase 2 – Understand & Reuse

**Goal:** Increase usefulness and stickiness using AI.

---

### US-7: Spend Credits for AI Processing
**As a user**, I want to use one credit per page to enhance my notes with AI, so that only important pages consume credits.

**Acceptance Criteria**
- Credits required are shown before processing
- Credits are deducted only on success

---

### US-8: Get AI Summary of Notes
**As a user**, I want a concise summary of my handwritten notes, so that I understand key points quickly.

**Acceptance Criteria**
- Summary is short and readable
- Summary references the original page

---

### US-9: Extract Tasks from Notes
**As a user**, I want tasks written in my notes to be identified, so that I can reuse them later.

**Acceptance Criteria**
- Tasks are clearly separated from normal text
- Task wording matches handwritten intent

---

### US-10: Share Notes Easily
**As a user**, I want to share my notes as clean text or summaries, so that I can send them via email or messaging apps.

**Acceptance Criteria**
- Sharing requires minimal steps
- Shared content is readable and formatted

---

## 4. Phase 3 – Personal & Intelligent

**Goal:** Build long-term differentiation and AI moat.

---

### US-11: Improve Accuracy Over Time
**As a user**, I want the app to better understand my handwriting over time, so that accuracy improves the more I use it.

**Acceptance Criteria**
- User corrections are remembered
- OCR accuracy improves for repeated patterns

---

### US-12: Group Related Notes Automatically
**As a user**, I want related notes to be grouped automatically, so that my ideas are organized without manual effort.

**Acceptance Criteria**
- Grouping feels logical
- User can manually override groups

---

### US-13: Personalised Suggestions
**As a user**, I want the app to suggest summaries or reminders based on my notes, so that nothing important is missed.

**Acceptance Criteria**
- Suggestions are relevant
- Notifications are controllable

---

## 5. Out-of-Scope (Explicit)

The following are **intentionally excluded** from early phases:
- Real-time handwriting recognition
- Unlimited AI processing
- Fully offline AI
- Automatic task execution

These may be considered in later versions.

---

## 6. How to Use This Document

- Product & design teams: validate UX against these stories
- Backend teams: derive APIs and data models
- AI teams: align OCR and enrichment capabilities
- Business teams: align pricing and cost controls

---

*This document will evolve as user feedback is collected.*

