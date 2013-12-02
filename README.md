Android-Scanner
===============

A small project for Mobile Computing at HTWG Konstanz

This project uses ZXing's core and android app (http://code.google.com/p/zxing/) as library.

For backward compatibility the android support libraries v4 and v7 are used.


Used permissions:

    android.permission.CAMERA:									Take pictures for scan.
    android.permission.FLASHLIGHT:								Activate flash if present.
    android.permission.INTERNET:								Product search if there is a result.
    android.permission.READ_CALENDAR:							Need access (read/write) to calendar to not write dublicate entries.
    android.permission.WRITE_CALENDAR:
    android.permission.READ_CONTACTS:							Need access (read/write) to contacts to not write dublicate entries.
    android.permission.WRITE_CONTACTS:
    com.android.browser.permission.READ_HISTORY_BOOKMARKS:		Need access (read/write) to bookmarks to not write dublicate entries.
    com.android.browser.permission.WRITE_HISTORY_BOOKMARKS:
    android.permission.WRITE_EXTERNAL_STORAGE:					Save history of scans.
    android.permission.WAKE_LOCK:								Deactivate standby.

Used Features:

    android.hardware.camera:				Filter devices without built-in camera. This feature is required.
    android.hardware.camera.autofocus:		Filter devices without autofocus capability. This feature is optional.
    android.hardware.camera.flash:			Filter devices without a flash. This feature is optional.

