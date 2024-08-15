# <center>Goods</center>

<p>
  Merupakan Aplikasi yang berfungsi untuk mengolah data asset.
</p>

## <u>Requirement</u>

Sebelum memulai, pastikan Anda memiliki hal-hal berikut:

- **Android Studio**: Versi terbaru atau versi yang direkomendasikan.
- **JDK**: Versi 17 atau lebih baru.
- **Android SDK**: minimum : SDK 31 (Android 12).
- **Koneksi Internet**: Untuk mengunduh dependensi dan plugin, serta berinteraksi dengan aplikasi.
- **Perangkat Smartphone Android** : dengan minimum versi Android yakni Android 12

## <u>Cara Instalasi Aplikasi via Android Studio</u>

Ikuti langkah-langkah berikut untuk menginstal dan menjalankan aplikasi:

1. **Clone Repository**:
   ```bash
   https://github.com/pangondion-k-naibaho/Goods.git

2. **Buka Project di Android Studio**:
   - Pilih "Open an existing Android Studio Project" dan arahkan ke folder project yang baru saja di-clone
4. **Sync Gradle**:
   - Klik "Sync Project with Gradle Files" di toolbar Android Studio untuk mengunduh dependensi yang diperlukan
6. **Konfigurasi Emulator atau Perangkat**:
   - Pilih emulator atau sambungkan perangkat Android untuk pengujian.
  
## <u>Cara Instalasi Aplikasi secara langsung ke perangkat Android</u>

1. **Unduh APK**:
   - APK Goods versi terbaru dapat diunduh di sini : https://bit.ly/3SM7qZ1
2. **Install APK**:
   - Simpan file .apk ini ke direktori lokal device Android
   - Buka file .apk dan ikuti petunjuk dari Android untuk instalasi.
   - Setelah terinstall, silahkan jalankan aplikasi dengan syarat device terhubung ke Internet

## <u>Cara Build APK</u>
Untuk membangun APK dari project, ikuti langkah-langkah berikut:
1. Buka Android Studio
2. Pilih **Build** dari menu bagian atas.
3. Pilih "**Build Bundle(s) / APK(s)**":
   - Klik "**Build APK(s)**" untuk menghasilkan file APK
4. Temukan APK:
   - Setelah proses selesai, APK dapat ditemukan di 'C:/Users/nama_user/AndroidStudioProjects/Goods/app/build/outputs/apk/debug/'

## <u>Struktur Aplikasi</u>
<b>1. 'src/main/java'</b>
- Merupakan tempat kode sumber Kotlin. terdiri atas beberapa paket seperti :
  - '**com.goods.client**': Package utama aplikasi
     - 'data' : berfungsi untuk penanganan data yang diperlukan untuk berjalannya aplikasi, berisikan model, remote, repository
     -  'ui' : berfungsi untuk penampilan data yang diperoleh dari komunikasi via WebService, bersisikan activities, custom component & adapter, dan viewmodel
  - '**res**' : Berisi resource yang mendukung pembuatan ui aplikasi, yang berisikan anim, drawable, font, layout, menu, mipmap, values, dan xml.
