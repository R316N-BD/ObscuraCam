# ObscuraCam fork: upstream issue sweep

Source: the 55 open issues on guardianproject/ObscuraCam (as of 2026-11-20).
Full triage, with a verdict for every issue: `doc/ISSUE_TRIAGE.md`.

## Constraint
This container can't download the Android SDK (dl.google.com is blocked), so a local
Gradle build can't run here. Verification = a GitHub Actions workflow on the fork
that builds the APK on every push (that workflow also covers #91 and #152).

## Plan
- [ ] A. Build modernization (#149, #103, #91): AGP 8.7 / Gradle 8.10 / JDK 17,
      compileSdk + targetSdk 35, namespace, android:exported, <queries> for camera,
      drop jcenter, fix the dead ndk{} block, NDK platform 21, buildConfig on,
      opt out of forced edge-to-edge
- [ ] B. Storage permissions on Android 10-15 (#133, #125, #149): READ_MEDIA_* on 33+,
      save through MediaStore on 29+ (no WRITE permission needed), share through FileProvider
- [ ] C. Opening images from "Others"/the document picker crashes (#140, #130): read EXIF from a
      stream, not a file path; keep the picker's URI permission; let the picker return videos too
- [ ] D. Sharing video fails (#129): FileProvider URIs instead of file:// (FileUriExposedException);
      copy content:// videos to the cache when there is no file path
- [ ] E. Output quality (#144): stop the hard-coded inSampleSize=2 (half resolution);
      add a JPEG quality setting
- [ ] F. Face detection on/off setting (#145) + "no faces found" hint (#111);
      run detection on a downscaled copy so full-resolution images stay fast
- [ ] G. Rotation (#84): handle 180 degrees and mirrored EXIF orientations, not only 90 and 270
- [ ] H. Region resize (#143): vertical-only drags were ignored (only X was checked); the
      minimum size was fixed in image pixels, so make it screen-relative
- [ ] I. Editor UI (#112, #109, #110): material action icons, no overflow menu, hide the zoom
      buttons (pinch-to-zoom already exists)
- [ ] J. Small bugs found while reading: newDefaultRegion wrote `left` twice;
      saveTmpImage could throw an NPE on mProgressDialog
- [ ] K. Docs: README (#90), APK checksum/verification (#152), CI workflow
- [ ] L. Triage doc for everything that isn't fixed (obsolete issues, design questions, big features)
- [ ] Verify: CI green, review the diff, then commit and push

## Review
(filled in at the end)
