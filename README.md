ObscuraCam
==========

ObscuraCam is an Android app for sharing photos and videos **without exposing the
people in them**. It is a project of [Guardian Project](https://guardianproject.info/apps/obscuracam/)
and [WITNESS](https://www.witness.org/).

"Secure" here means three things:

- **Hide identities.** Faces are found automatically (you can turn this off in
  Settings), and you can mark any other area yourself: number plates, name tags,
  street signs. Each area can be pixelated, blurred, blacked out, or covered with a mask.
- **Strip metadata.** Saved photos are re-encoded from pixels only, so the original's
  EXIF data (GPS location, camera model, serial numbers, timestamps) is not carried over.
- **Keep work local.** Editing happens on the device. Nothing is uploaded.

Videos can be pixelated or posterized as a whole, and the audio track can be muted.

What it does not protect against
--------------------------------
Obscuring pixels does not remove every way of identifying a photo. Clothing, tattoos,
backgrounds and voices can still identify people, and image sensors leave a faint
"noise fingerprint" that can link photos to the same camera
([#102](https://github.com/guardianproject/ObscuraCam/issues/102)). Think about who could
recognise what is left in the picture.

Building
--------
Requirements: JDK 17 and the Android SDK (with the NDK, which Gradle installs automatically
if your SDK licences are accepted).

```
./gradlew assembleDebug
```

The APK is written to `app/build/outputs/apk/debug/`. Every push is also built by GitHub
Actions (`.github/workflows/build.yml`); each run publishes the APK and its SHA-256 checksum.

Verifying a downloaded APK
--------------------------
Compare the file checksum with the one published next to the download:

```
sha256sum ObscuraCam-*.apk
```

To check who signed it, compare the signing certificate fingerprint with the one the
maintainers publish:

```
apksigner verify --print-certs ObscuraCam-*.apk
```

Only trust a fingerprint that comes from the maintainers themselves (their website, F-Droid,
or a signed release note), not one posted in an issue or forum.

Images
------
Mr Smiley Face: https://commons.wikimedia.org/wiki/File:Mr._Smiley_Face.svg
