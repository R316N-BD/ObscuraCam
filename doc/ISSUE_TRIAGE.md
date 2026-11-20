# Upstream issue triage

Every open issue on [guardianproject/ObscuraCam](https://github.com/guardianproject/ObscuraCam/issues)
(55 as of 2026-11-20), sorted by what this fork did about it.

Status key:
- **Fixed**: code change on this branch, with the root cause found in the code
- **Fixed, needs a device check**: the code path is fixed, but the original report has no stack
  trace, so confirm on a phone
- **Already done**: the current code already does this, so the issue can be closed
- **Obsolete**: reported against code that has since been removed or rewritten
- **Open**: still to do (see "What could be added" at the end)

## Fixed on this branch

| # | Issue | Root cause | Fix |
|---|-------|------------|-----|
| 149 | Not compatible with new Android | targetSdk 30. Android 14+ refuses to install or auto-update apps that target very old SDKs | targetSdk/compileSdk 35, AGP 8.7, Gradle 8.10, `android:exported`, `<queries>`, edge-to-edge opt-out |
| 103 | Build fails (dependency missing) | jcenter shut down; old AGP/Gradle | Build moved to Google + Maven Central; the CI workflow proves it builds |
| 91 | APK out of date | No automated builds | CI builds an APK on every push |
| 152 | SHA-256 for the APK | No checksums published | CI prints the SHA-256 in the run summary; the README explains `sha256sum` and `apksigner` |
| 140 | Crash opening an image from the dialog | EXIF was read from `cursor.getString(DATA)`, which is `null` for document-picker URIs, so `new File(null)` threw an NPE | EXIF is read from a stream (`androidx.exifinterface`); a failed decode shows a message instead of crashing |
| 130 | "Others" album returns to the home screen | Same crash as #140 (the picker gives `content://…documents/…` URIs) | Same fix, plus a persistable URI grant |
| 133 | Share fails: "cannot write tempfile" | The temp file went to external storage, which needs a permission share never asked for, and `file://` URIs are blocked on Android 7+ | Temp file goes to the app cache and is shared through `FileProvider`; no permission needed |
| 129 | Cannot share video | `Uri.fromFile()` in `ACTION_SEND` throws `FileUriExposedException` on Android 7+ | `FileProvider` URI with a read grant |
| 144 | Output is downscaled | `inSampleSize = 2` was hard-coded, halving every photo | Decode at full resolution up to 24 MP, halving only on out-of-memory; new JPEG quality setting |
| 145 | Let me turn face detection off | `autodetect = true` was hard-coded | New Settings screen (main screen menu): "Detect faces automatically" |
| 111 | Hint if no faces are found | The toast was commented out | Toast: "N faces found", or "No faces found. Tap the photo to add an area" |
| 84 | Portrait photos appear rotated | Only EXIF 90/270 were handled, and EXIF was read from a file path that often doesn't exist | All 8 EXIF orientations, read from a stream |
| 143 | Area resizing unreliable when zoomed | (1) movement was only checked on X, so vertical drags were ignored; (2) the minimum size was 50 *image* pixels | Checks both axes; minimum size is 50 *screen* pixels at any zoom |
| 112 | Material icons | Text-only menu items | Material share / preview / save icons |
| 110 | Remove overflow menu from editor | All 3 actions were in or near the overflow | All 3 are toolbar icons now |
| 90 | README not descriptive | — | README explains what "secure" means, its limits, building and verifying |
| 125 | Freezes after launch | Likely: on Android 11+ the app asked for `WRITE_EXTERNAL_STORAGE` and required it, but it's never granted there, so the gallery never loaded | **Fixed, needs a device check.** Asks for the right permission per Android version |
| 101 | Fails to save large photos | Out of memory plus encoding on the UI thread | **Fixed, needs a device check.** Saving runs on a background thread with OOM handling |
| 69 | ANR in face detection | Detection on the UI thread, at full size | Already threaded upstream; detection now also runs on a ≤1024 px copy |

Bugs found while reading the code (no issue filed):
- The committed `gradle-wrapper.jar` matched **no official Gradle release**. Replaced with the
  official 8.10.2 wrapper (this matters for a security-focused app).
- `FaceDetector` throws `IllegalArgumentException` on odd-width bitmaps; only NPE was caught.
- `newDefaultRegion()` assigned the bottom edge to `left`.
- Save output streams were never closed.
- `VideoEditor` crashed when opened without a video (`savedInstanceState` null).
- Saving to MediaStore with `video/*` (a wildcard type) would be rejected; now `video/mp4`.
- The camera button did nothing on Android 11+ (no `<queries>` entry for `IMAGE_CAPTURE`).

## Already done (can be closed upstream)

| # | Issue | Evidence |
|---|-------|----------|
| 109 | Remove zoom buttons | The zoom buttons' container is `visibility="gone"` in `imageviewer.xml`; pinch-to-zoom works |
| 118 | Remove audio from video | The video editor has a "Mute" checkbox (`cb_audio_mute`) |
| 13 | Tagging UI | The region editor *is* the tagging UI |
| 132 | Swedish translation | Translations moved from Transifex to Weblate; submit there |

## Obsolete (reported against removed code)

| # | Issue | Why it's obsolete |
|---|-------|-------------------|
| 70 | `BadTokenException` in `QuickAction` popup | The QuickAction popup library is gone; options are a RecyclerView now |
| 83 | App closes picking from gallery (Android 3.1) | Gallery was rewritten; the remaining crash path was #140, now fixed |
| 87 | Region dialog pops up on every tap | Popup dialog replaced by the bottom options bar |
| 88 | Areas wrong size/position when saved | Was the native JPEG path with sample-size scaling; saving now renders the same bitmap the preview shows |
| 89 | No manipulation of regions on save | Native redaction path; it's disabled (commented out) |
| 115 | Native JPEG redact crashes | Native redaction is disabled; saving uses the Java path |
| 97 | `CursorWindow` column -1 | Old loader code; loading no longer queries the `DATA` column |
| 78, 98 | Filenames / delete original | The "replace original" and "delete original" flows were removed (code commented out) |
| 7 | Pixelpipe-style popups | Pixelpipe no longer exists |

## Needs a reproduction

| # | Issue | Notes |
|---|-------|-------|
| 131 | Crash rotating after taking a photo with PicsArt | No stack trace. Retest with this build; if it still crashes, capture `adb logcat` |
| 134 | Video/GIF stuck "saving" (Android 7.1) | GIFs aren't supported as video. For MP4, the GPU encoder may lack a codec on that device. Needs logcat |

## Open: design decisions for maintainers

| # | Issue | Suggested next step |
|---|-------|---------------------|
| 104, 105, 106, 108 | 2017 UX mockups (workflow, handles, option bar, tap-outside preview) | Partly implemented. #108 (tap outside image to preview) is small |
| 120 | Accept/Refuse text on startup | Replace with a one-screen onboarding |
| 121 | Hide ObscuraCam's own output from the main gallery | Easy (filter the `Pictures/ObscuraCam` folder), but people may want to re-share; make it a setting |
| 122 | Style the video indicator | Small icon/drawable change |
| 135 | Update icon and menu | Needs design assets |
| 147 | Light theme | The editor is dark on purpose (photo contrast); add a DayNight theme for the other screens |
| 153 | Project status | For the maintainers to answer |

## What could be added

Ordered by value for the people this app protects.

1. **Obscure faces in video (#148).** The most-asked feature. The video code already has region
   trails (`RegionTrail`, `ObscureRegion`) but they aren't wired into the GPU renderer. Needs
   per-frame face tracking plus a masked GPU filter. Large.
2. **Sanitize video (#126).** Re-mux into a fresh container so location and device metadata in
   the MP4 `udta`/`meta` boxes are dropped even when nothing is pixelated. Medium.
3. **Better face detection (#6).** `android.media.FaceDetector` is from 2008 and misses profile
   and small faces. OpenCV's DNN with the YuNet model (Apache-2.0) is far more accurate and stays
   F-Droid compatible; ML Kit is not FOSS. Medium.
4. **Sensor-noise fingerprint countermeasures (#102).** Downscaling plus re-noising weakens
   PRNU camera fingerprinting. Research needed; at minimum, document the risk (done in the README).
5. **Reduce audio bitrate / distort voice (#119)**, **reduce frame size (#127).** Medium,
   inside the existing video pipeline.
6. **Drop the `INTERNET` permission.** The app makes no network calls (no networking code
   anywhere in `src/`), so the permission only widens the attack surface. Small, but needs a
   check that the image library never fetches a URL.
7. **Panic button (#9)** through [PanicKit](https://guardianproject.info/code/panickit/), and a
   **secure media store (#11)** for encrypted originals. Large.
8. **Auto-zoom on tiny regions (#64).** Small, now that resizing works when zoomed in (#143).
9. **Tests.** There are no unit or instrumentation tests. Good first targets:
   `ImageEditor.matrixForExifOrientation`, `MediaPermissions`, region min-size math.
