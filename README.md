Android-Scanner
===============

A small project for Mobile Computing at HTWG Konstanz

This project uses ZXing's core and android app (http://code.google.com/p/zxing/) as library.

For backward compatibility the android support libraries v4 and v7 are used.
With this it is possible to support API level 10 (Android 2.3.3 and up).


## Used permissions:

<table>
  <tr>
    <th>Permission</th>
    <th>Description</th>
  </tr>
  <tr>
    <td>android.permission.CAMERA</td>
    <td>Take pictures for scan.</td>
  </tr>
  <tr>
    <td>android.permission.FLASHLIGHT</td>
    <td>Activate flash if present.</td>
  </tr>
  <tr>
    <td>android.permission.INTERNET</td>
    <td>Product search if there is a result.</td>
  </tr>
  <tr>
    <td>android.permission.READ_CALENDAR <br/>
        android.permission.WRITE_CALENDAR</td>
    <td>Need access (read/write) to calendar to not write dublicate entries.</td>
  </tr>
  <tr>
    <td>android.permission.READ_CONTACTS <br/>
        android.permission.WRITE_CONTACTS</td>
    <td>Need access (read/write) to contacts to not write dublicate entries.</td>
  </tr>
  <tr>
    <td>com.android.browser.permission.READ_HISTORY_BOOKMARKS <br/>
        com.android.browser.permission.WRITE_HISTORY_BOOKMARKS</td>
    <td>Need access (read/write) to bookmarks to not write dublicate entries.</td>
  </tr>
  <tr>
    <td>android.permission.WRITE_EXTERNAL_STORAGE</td>
    <td>Save history of scans.</td>
  </tr>
  <tr>
    <td>android.permission.WAKE_LOCK</td>
    <td>Deactivate standby.</td>
  </tr>
</table>

## Used Features:

<table>
  <tr>
    <th>Feature</th>
    <th>Description</th>
  </tr>
  <tr>
    <td>android.hardware.camera</td>
    <td>Filter devices without built-in camera. This feature is required.</td>
  </tr>
  <tr>
    <td>android.hardware.camera.autofocus</td>
    <td>Filter devices without autofocus capability. This feature is optional.</td>
  </tr>
  <tr>
    <td>android.hardware.camera.flash</td>
    <td>Filter devices without a flash. This feature is optional.</td>
  </tr>
</table>

## TODO's

- [x] Basic structure & design
- [ ] Save & display history.
- [ ] Implement barcode part.
- [ ] Implement QR-Code part.
- [ ] Resolve dependencies and add them to repository.
- [ ] Write tests.
