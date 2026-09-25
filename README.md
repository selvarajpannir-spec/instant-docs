# Insta Docs – V1

Offline Android app that turns a few fields into a signed, print-ready PDF in under 60 seconds.

**All features free. No ads. No tracking. No internet permission.**

## V1 Content (16 templates)

### 1. Resignation Letters
- Standard Notice
- With Gratitude
- Immediate Resignation
- Personal Reasons

### 2. Invoices & Payments
- Quotation
- Invoice
- Payment Reminder
- Receipt

### 3. Leave & Absence
- Leave Application
- Sick Leave
- School Absence Note
- Extended Leave Request

### 4. Authorization Letters
- General Authorization
- Collect Documents / Parcel
- Bank Authorization
- Child Pickup Authorization

## Features
- Fully offline (no INTERNET permission)
- Drawn signature with blue ink (default) or black
- Profile memory (name, designation, company etc. remembered)
- Live preview of the exact document that will be printed
- Share / download PDF via system share sheet
- Clean, mobile-first UI

## How to build
1. Push all files in this folder to the **root** of a GitHub repository.
2. The `build.yml` workflow will:
   - Organise files into a standard Gradle project
   - Build debug + unsigned release APKs
   - Upload them as artifacts

## File list (flat repo)
- `AndroidManifest.xml`
- `MainActivity.kt`
- `AppBridge.kt`
- `build.gradle`
- `proguard-rules.pro`
- `file_paths.xml`
- `index.html`          ← the entire app UI + templates
- `build.yml`           ← GitHub Actions workflow
- `README.md`

Optional: add `icon.png` (1024×1024) at the root for a proper launcher icon.

## Notes
- Application ID: `com.instadocs.app`
- minSdk 23, targetSdk 36
- Signature is captured on a canvas and embedded as PNG in the document
- PDF generation uses WebView's print adapter
