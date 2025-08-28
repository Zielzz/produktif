-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 14 Jun 2025 pada 15.48
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_cucimobil`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `absensi`
--

CREATE TABLE `absensi` (
  `id_absensi` int(11) NOT NULL,
  `id_karyawan` int(11) NOT NULL,
  `waktu_masuk` datetime DEFAULT NULL,
  `waktu_pulang` datetime DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `keterangan` varchar(40) DEFAULT NULL,
  `is_delete` tinyint(1) DEFAULT 0,
  `tanggal` date NOT NULL DEFAULT curdate(),
  `durasi` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `absensi`
--

INSERT INTO `absensi` (`id_absensi`, `id_karyawan`, `waktu_masuk`, `waktu_pulang`, `status`, `keterangan`, `is_delete`, `tanggal`, `durasi`) VALUES
(63, 1, '2025-05-15 15:52:25', '2025-05-15 15:54:25', 'Pulang', '', 0, '2025-05-15', 0.03),
(66, 4, '2025-05-15 15:57:24', '2025-05-15 15:57:24', 'Masuk', '', 0, '2025-05-15', 0.00),
(73, 4, '2025-05-15 16:09:28', '2025-05-15 16:09:28', 'Masuk', '', 0, '2025-05-15', 0.00),
(74, 4, '2025-05-15 16:09:32', '2025-05-15 16:09:32', 'Masuk', '', 0, '2025-05-15', 0.00),
(87, 4, '2025-05-18 22:09:57', NULL, 'Masuk', NULL, 0, '2025-05-18', NULL),
(88, 4, '2025-05-19 08:08:16', '2025-05-19 08:08:16', 'Masuk', '', 0, '2025-05-19', 0.00),
(89, 4, '2025-05-19 08:08:20', '2025-05-19 08:08:20', 'Masuk', '', 0, '2025-05-19', 0.00),
(90, 4, '2025-05-19 08:58:13', '2025-05-19 08:58:13', 'Masuk', '', 0, '2025-05-19', 0.00),
(91, 4, '2025-05-19 08:58:20', '2025-05-19 08:58:20', 'Masuk', '', 0, '2025-05-19', 0.00),
(92, 4, '2025-05-19 17:40:47', '2025-05-19 17:40:47', 'Masuk', '', 0, '2025-05-19', 0.00),
(93, 2, '2025-05-19 21:55:51', '2025-05-19 21:55:51', 'Masuk', '', 0, '2025-05-19', 0.00),
(94, 2, '2025-05-21 12:25:10', '2025-05-21 12:25:10', 'Masuk', '', 0, '2025-05-21', 0.00),
(96, 9, '2025-05-26 20:17:58', '2025-05-26 20:18:01', 'Pulang', 'HADIR: Absen Pulang', 0, '2025-05-26', 0.00),
(97, 10, '2025-05-26 20:50:01', '2025-05-26 20:50:03', 'HADIR', 'HADIR: Absen Pulang', 0, '2025-05-26', 0.00),
(98, 11, NULL, NULL, 'TIDAK HADIR', 'SAKIT', 0, '2025-05-26', NULL),
(99, 12, '2025-05-27 21:18:12', '2025-05-27 21:21:36', 'HADIR', 'Terlambat Masuk', 0, '2025-05-27', 0.05),
(100, 13, '2025-05-27 21:40:05', '2025-05-27 21:41:56', 'HADIR', 'Terlambat Masuk', 0, '2025-05-27', 0.02),
(101, 14, '2025-05-27 21:54:44', '2025-05-27 21:58:31', 'HADIR', 'Terlambat Masuk', 0, '2025-05-27', 0.05),
(103, 1, '2025-05-31 21:17:21', '2025-05-31 21:18:37', 'HADIR', 'Terlambat Masuk', 0, '2025-05-31', 0.02),
(104, 9, '2025-06-05 11:15:37', NULL, 'HADIR', 'Terlambat Masuk', 0, '2025-06-05', NULL);

-- --------------------------------------------------------

--
-- Struktur dari tabel `barang`
--

CREATE TABLE `barang` (
  `id_barang` int(11) NOT NULL,
  `nama_barang` varchar(45) NOT NULL,
  `satuan` varchar(45) NOT NULL,
  `stok` int(11) NOT NULL,
  `harga_satuan` decimal(15,2) NOT NULL DEFAULT 0.00,
  `insert_by` int(11) DEFAULT NULL,
  `update_by` int(11) DEFAULT NULL,
  `delete_by` int(11) DEFAULT NULL,
  `insert_at` datetime NOT NULL DEFAULT current_timestamp(),
  `update_at` datetime DEFAULT NULL,
  `delete_at` datetime DEFAULT NULL,
  `is_delete` tinyint(4) NOT NULL DEFAULT 0,
  `isi_sachet` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data untuk tabel `barang`
--

INSERT INTO `barang` (`id_barang`, `nama_barang`, `satuan`, `stok`, `harga_satuan`, `insert_by`, `update_by`, `delete_by`, `insert_at`, `update_at`, `delete_at`, `is_delete`, `isi_sachet`) VALUES
(2, 'Shampo Mobil', 'sachet', 8100, 2500.00, 1, 1, NULL, '2025-03-07 17:27:36', '2025-06-14 20:41:38', NULL, 0, 100.00),
(3, 'Lap Microfiber', 'pcs', 250, 15000.00, 1, NULL, NULL, '2025-03-07 17:27:36', NULL, NULL, 0, NULL),
(4, 'Wax Premium', 'botol', 13, 25000.00, 1, 1, NULL, '2025-03-07 17:27:36', '2025-06-05 10:13:22', NULL, 0, NULL),
(5, 'Semir Ban', 'sachet', 700, 2500.00, 1, 1, NULL, '2025-03-07 17:27:36', '2025-06-05 08:54:32', NULL, 0, 15.00),
(8, 'kain', 'pcs', 102, 10000.00, 1, 1, NULL, '2025-05-26 09:43:27', '2025-06-14 20:01:35', NULL, 0, 1.00),
(11, 'tes', '1', 1, 1.00, 1, NULL, NULL, '2025-06-14 20:48:19', NULL, NULL, 0, 1.00);

-- --------------------------------------------------------

--
-- Struktur dari tabel `biaya_operasional`
--

CREATE TABLE `biaya_operasional` (
  `id_biaya` int(11) NOT NULL,
  `deskripsi` varchar(255) NOT NULL,
  `jumlah_biaya` decimal(15,2) NOT NULL,
  `tanggal_biaya` date NOT NULL DEFAULT curdate(),
  `insert_by` int(11) DEFAULT NULL,
  `insert_at` datetime NOT NULL DEFAULT current_timestamp(),
  `is_delete` tinyint(4) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data untuk tabel `biaya_operasional`
--

INSERT INTO `biaya_operasional` (`id_biaya`, `deskripsi`, `jumlah_biaya`, `tanggal_biaya`, `insert_by`, `insert_at`, `is_delete`) VALUES
(6, 'tes', 1000000.00, '2025-06-05', 1, '2025-06-05 11:19:47', 0);

-- --------------------------------------------------------

--
-- Struktur dari tabel `detail_opname_stok`
--

CREATE TABLE `detail_opname_stok` (
  `id_detail_opname` int(11) NOT NULL,
  `id_opname` int(11) NOT NULL,
  `id_barang` int(11) NOT NULL,
  `stok_sistem` int(11) NOT NULL,
  `stok_fisik` int(11) NOT NULL,
  `selisih` int(11) GENERATED ALWAYS AS (`stok_fisik` - `stok_sistem`) STORED
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `detail_opname_stok`
--

INSERT INTO `detail_opname_stok` (`id_detail_opname`, `id_opname`, `id_barang`, `stok_sistem`, `stok_fisik`) VALUES
(1, 1, 2, 8944, 8000),
(2, 1, 3, 260, 250),
(3, 1, 4, 14, 13),
(4, 1, 5, 799, 700),
(5, 1, 8, 10, 9);

-- --------------------------------------------------------

--
-- Struktur dari tabel `fasilitas`
--

CREATE TABLE `fasilitas` (
  `id_fasilitas` int(11) NOT NULL,
  `nama_fasilitas` varchar(45) NOT NULL,
  `insert_by` int(11) DEFAULT NULL,
  `update_by` int(11) DEFAULT NULL,
  `delete_by` int(11) DEFAULT NULL,
  `insert_at` datetime NOT NULL DEFAULT current_timestamp(),
  `update_at` datetime DEFAULT NULL,
  `delete_at` datetime DEFAULT NULL,
  `is_delete` tinyint(4) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data untuk tabel `fasilitas`
--

INSERT INTO `fasilitas` (`id_fasilitas`, `nama_fasilitas`, `insert_by`, `update_by`, `delete_by`, `insert_at`, `update_at`, `delete_at`, `is_delete`) VALUES
(1, 'Cuci Salju', 1, 1, NULL, '2025-03-07 17:27:56', '2025-06-05 10:15:17', NULL, 0),
(2, 'Cuci Interior', 1, 1, NULL, '2025-03-07 17:27:56', '2025-06-05 10:15:30', NULL, 0),
(3, 'Poles Body', 1, 1, NULL, '2025-03-07 17:27:56', '2025-06-05 10:15:39', NULL, 0),
(4, 'Poles Kaca', 1, 1, NULL, '2025-03-07 17:27:56', '2025-06-05 10:15:42', NULL, 0),
(5, 'Pembersihan Mesin', 1, 1, NULL, '2025-03-07 17:27:56', '2025-06-05 10:15:49', NULL, 0);

-- --------------------------------------------------------

--
-- Struktur dari tabel `fasilitas_detail`
--

CREATE TABLE `fasilitas_detail` (
  `id_fasilitas` int(11) NOT NULL,
  `id_barang` int(11) NOT NULL,
  `jumlah` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data untuk tabel `fasilitas_detail`
--

INSERT INTO `fasilitas_detail` (`id_fasilitas`, `id_barang`, `jumlah`) VALUES
(1, 2, 2),
(2, 2, 1),
(2, 3, 1),
(3, 4, 1),
(4, 4, 1),
(5, 2, 2);

-- --------------------------------------------------------

--
-- Struktur dari tabel `kartu_stok`
--

CREATE TABLE `kartu_stok` (
  `id_kartu_stok` int(11) NOT NULL,
  `id_barang` int(11) NOT NULL,
  `tanggal` datetime NOT NULL DEFAULT current_timestamp(),
  `tipe` enum('MASUK','KELUAR') NOT NULL,
  `jumlah` int(11) NOT NULL,
  `keterangan` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `kartu_stok`
--

INSERT INTO `kartu_stok` (`id_kartu_stok`, `id_barang`, `tanggal`, `tipe`, `jumlah`, `keterangan`) VALUES
(135, 2, '2025-06-05 00:00:00', 'KELUAR', 2, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060002'),
(136, 2, '2025-06-05 00:00:00', 'KELUAR', 1, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060002'),
(137, 3, '2025-06-05 00:00:00', 'KELUAR', 1, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060002');

-- --------------------------------------------------------

--
-- Struktur dari tabel `karyawan`
--

CREATE TABLE `karyawan` (
  `id_karyawan` int(11) NOT NULL,
  `nama_karyawan` varchar(45) NOT NULL,
  `telepon` varchar(45) NOT NULL,
  `alamat` varchar(255) NOT NULL,
  `rfid` varchar(25) DEFAULT NULL,
  `insert_by` int(11) DEFAULT NULL,
  `update_by` int(11) DEFAULT NULL,
  `delete_by` int(11) DEFAULT NULL,
  `insert_at` datetime NOT NULL DEFAULT current_timestamp(),
  `update_at` datetime DEFAULT NULL,
  `delete_at` datetime DEFAULT NULL,
  `is_delete` tinyint(4) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data untuk tabel `karyawan`
--

INSERT INTO `karyawan` (`id_karyawan`, `nama_karyawan`, `telepon`, `alamat`, `rfid`, `insert_by`, `update_by`, `delete_by`, `insert_at`, `update_at`, `delete_at`, `is_delete`) VALUES
(1, 'Budi Santoso', '081234567890', 'Jl. Merdeka No.1, Jakarta', '54321', 1, NULL, NULL, '2025-03-07 17:28:14', NULL, NULL, 0),
(2, 'Doni Setiawan', '082345678901', 'Jl. Sudirman No.10, Bandung', '0123', 1, NULL, NULL, '2025-03-07 17:28:14', NULL, NULL, 0),
(3, 'Siti Rohmah', '083456789012', 'Jl. Diponegoro No.20, Bekasi', '5678', 1, 1, NULL, '2025-03-07 17:28:14', '2025-05-19 21:09:03', NULL, 0),
(4, 'Rudi Hartono', '084567890123', 'Jl. Thamrin No.5, Tangerang', '123', 1, 1, NULL, '2025-03-07 17:28:14', '2025-05-05 09:40:30', NULL, 0),
(9, 'tes', '1234567890', 'tes', '3960865849', 1, NULL, NULL, '2025-06-05 11:15:27', NULL, NULL, 0);

-- --------------------------------------------------------

--
-- Struktur dari tabel `layanan`
--

CREATE TABLE `layanan` (
  `id_layanan` int(11) NOT NULL,
  `nama_layanan` varchar(45) NOT NULL,
  `harga` decimal(15,2) NOT NULL,
  `insert_by` int(11) DEFAULT NULL,
  `update_by` int(11) DEFAULT NULL,
  `delete_by` int(11) DEFAULT NULL,
  `insert_at` datetime NOT NULL DEFAULT current_timestamp(),
  `update_at` datetime DEFAULT NULL,
  `delete_at` datetime DEFAULT NULL,
  `is_delete` tinyint(4) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data untuk tabel `layanan`
--

INSERT INTO `layanan` (`id_layanan`, `nama_layanan`, `harga`, `insert_by`, `update_by`, `delete_by`, `insert_at`, `update_at`, `delete_at`, `is_delete`) VALUES
(1, 'Cuci Mobil Reguler', 35000.00, 1, NULL, NULL, '2025-03-07 17:28:27', NULL, NULL, 0),
(2, 'Cuci Mobil Premium', 50000.00, 1, NULL, NULL, '2025-03-07 17:28:27', NULL, NULL, 0),
(3, 'Poles Body', 75000.00, 1, NULL, NULL, '2025-03-07 17:28:27', NULL, NULL, 0),
(4, 'Cuci Motor', 20000.00, 1, NULL, NULL, '2025-03-07 17:28:27', NULL, NULL, 0);

-- --------------------------------------------------------

--
-- Struktur dari tabel `layanan_detail`
--

CREATE TABLE `layanan_detail` (
  `id_layanan` int(11) NOT NULL,
  `id_fasilitas` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data untuk tabel `layanan_detail`
--

INSERT INTO `layanan_detail` (`id_layanan`, `id_fasilitas`) VALUES
(1, 1),
(2, 1),
(2, 2),
(3, 3),
(3, 4),
(4, 1);

-- --------------------------------------------------------

--
-- Struktur dari tabel `log_barang_masuk`
--

CREATE TABLE `log_barang_masuk` (
  `id_log` int(11) NOT NULL,
  `id_barang` int(11) NOT NULL,
  `nama_barang` varchar(255) NOT NULL,
  `satuan` varchar(50) NOT NULL,
  `stok` int(11) NOT NULL,
  `harga_satuan` decimal(15,2) NOT NULL,
  `id_users` int(11) NOT NULL,
  `tanggal_masuk` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data untuk tabel `log_barang_masuk`
--

INSERT INTO `log_barang_masuk` (`id_log`, `id_barang`, `nama_barang`, `satuan`, `stok`, `harga_satuan`, `id_users`, `tanggal_masuk`) VALUES
(14, 2, 'Shampo Mobil', 'sachet', 50, 2500.00, 1, '2025-06-14 20:41:38'),
(15, 11, 'tes', '1', 1, 1.00, 1, '2025-06-14 20:48:19');

-- --------------------------------------------------------

--
-- Struktur dari tabel `opname_stok`
--

CREATE TABLE `opname_stok` (
  `id_opname` int(11) NOT NULL,
  `tanggal_opname` date NOT NULL DEFAULT curdate(),
  `keterangan` varchar(100) DEFAULT NULL,
  `insert_by` int(11) DEFAULT NULL,
  `insert_at` datetime NOT NULL DEFAULT current_timestamp(),
  `is_delete` tinyint(4) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `opname_stok`
--

INSERT INTO `opname_stok` (`id_opname`, `tanggal_opname`, `keterangan`, `insert_by`, `insert_at`, `is_delete`) VALUES
(1, '2025-06-05', 'Kehilangan', 1, '2025-06-05 11:18:02', 0);

-- --------------------------------------------------------

--
-- Struktur dari tabel `pelanggan`
--

CREATE TABLE `pelanggan` (
  `id_pelanggan` char(13) NOT NULL,
  `nama_pelanggan` varchar(45) NOT NULL,
  `telepon` varchar(45) NOT NULL,
  `alamat` varchar(255) NOT NULL,
  `member_status` enum('aktif','tidak aktif') NOT NULL DEFAULT 'tidak aktif',
  `insert_by` int(11) DEFAULT NULL,
  `update_by` int(11) DEFAULT NULL,
  `delete_by` int(11) DEFAULT NULL,
  `insert_at` datetime NOT NULL DEFAULT current_timestamp(),
  `update_at` datetime DEFAULT NULL,
  `delete_at` datetime DEFAULT NULL,
  `is_delete` tinyint(4) NOT NULL DEFAULT 0,
  `jumlah_transaksi` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data untuk tabel `pelanggan`
--

INSERT INTO `pelanggan` (`id_pelanggan`, `nama_pelanggan`, `telepon`, `alamat`, `member_status`, `insert_by`, `update_by`, `delete_by`, `insert_at`, `update_at`, `delete_at`, `is_delete`, `jumlah_transaksi`) VALUES
('CR20250605001', 'eric', '098789876754', 'Jl.Mujahir Gg.03', 'aktif', 1, 1, NULL, '2025-06-05 08:59:02', '2025-06-05 11:13:49', NULL, 0, 0),
('CR20250605002', 'Eric', '098767897654', 'Jl.Mujahir Gg.03 No.16', 'tidak aktif', 1, NULL, NULL, '2025-06-05 11:12:08', NULL, NULL, 0, 0);

-- --------------------------------------------------------

--
-- Struktur dari tabel `pengeluaran_barang`
--

CREATE TABLE `pengeluaran_barang` (
  `id_pengeluaran` int(11) NOT NULL,
  `id_barang` int(11) NOT NULL,
  `jumlah` int(11) NOT NULL,
  `harga_total` decimal(15,2) NOT NULL,
  `tipe_pengeluaran` enum('Pembelian','Penggunaan') NOT NULL,
  `tanggal_pengeluaran` date NOT NULL DEFAULT curdate(),
  `keterangan` varchar(255) DEFAULT NULL,
  `insert_by` int(11) DEFAULT NULL,
  `insert_at` datetime NOT NULL DEFAULT current_timestamp(),
  `is_delete` tinyint(4) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data untuk tabel `pengeluaran_barang`
--

INSERT INTO `pengeluaran_barang` (`id_pengeluaran`, `id_barang`, `jumlah`, `harga_total`, `tipe_pengeluaran`, `tanggal_pengeluaran`, `keterangan`, `insert_by`, `insert_at`, `is_delete`) VALUES
(197, 2, 2, 5000.00, 'Penggunaan', '2025-06-05', 'Digunakan untuk pesanan 25060002', 1, '2025-06-05 11:12:08', 0),
(198, 2, 1, 2500.00, 'Penggunaan', '2025-06-05', 'Digunakan untuk pesanan 25060002', 1, '2025-06-05 11:12:08', 0),
(199, 3, 1, 15000.00, 'Penggunaan', '2025-06-05', 'Digunakan untuk pesanan 25060002', 1, '2025-06-05 11:12:08', 0);

--
-- Trigger `pengeluaran_barang`
--
DELIMITER $$
CREATE TRIGGER `after_pengeluaran_barang_insert_update_price` AFTER INSERT ON `pengeluaran_barang` FOR EACH ROW BEGIN
    IF NEW.tipe_pengeluaran = 'Pembelian' THEN
        UPDATE barang
        SET stok = stok + NEW.jumlah,
            harga_satuan = NEW.harga_total / NEW.jumlah
        WHERE id_barang = NEW.id_barang;
    END IF;
    -- Remove stock update for 'Penggunaan' to avoid conflict
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `trg_pengeluaran_barang` AFTER INSERT ON `pengeluaran_barang` FOR EACH ROW BEGIN
    INSERT INTO kartu_stok (
        id_barang, tanggal, tipe, jumlah, keterangan
    )
    VALUES (
        NEW.id_barang, 
        NEW.tanggal_pengeluaran, 
        'KELUAR', 
        NEW.jumlah, 
        CONCAT('Pengeluaran tipe: ', NEW.tipe_pengeluaran, ', Keterangan: ', IFNULL(NEW.keterangan, ''))
    );
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Struktur dari tabel `pesanan`
--

CREATE TABLE `pesanan` (
  `id_pesanan` char(8) NOT NULL,
  `tanggal_pesanan` date NOT NULL,
  `merk_mobil` varchar(45) NOT NULL,
  `nomor_plat` varchar(45) NOT NULL,
  `total_harga` decimal(15,2) DEFAULT NULL,
  `status` varchar(45) NOT NULL,
  `id_pelanggan` char(13) NOT NULL,
  `id_layanan` int(11) NOT NULL,
  `no_antrian` int(11) DEFAULT NULL,
  `insert_by` int(11) DEFAULT NULL,
  `update_by` int(11) DEFAULT NULL,
  `delete_by` int(11) DEFAULT NULL,
  `insert_at` datetime NOT NULL DEFAULT current_timestamp(),
  `update_at` datetime DEFAULT NULL,
  `delete_at` datetime DEFAULT NULL,
  `is_delete` tinyint(4) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data untuk tabel `pesanan`
--

INSERT INTO `pesanan` (`id_pesanan`, `tanggal_pesanan`, `merk_mobil`, `nomor_plat`, `total_harga`, `status`, `id_pelanggan`, `id_layanan`, `no_antrian`, `insert_by`, `update_by`, `delete_by`, `insert_at`, `update_at`, `delete_at`, `is_delete`) VALUES
('25060001', '2025-06-05', 'Civic Turbo', 'E1221IC', 50000.00, 'Selesai', 'CR20250605001', 2, 1, 1, 1, NULL, '2025-06-05 08:59:02', '2025-06-05 08:59:14', NULL, 0),
('25060002', '2025-06-05', 'Suv', 'E121IC', 50000.00, 'Selesai', 'CR20250605002', 2, 2, 1, 1, NULL, '2025-06-05 11:12:08', '2025-06-05 11:12:56', NULL, 0);

--
-- Trigger `pesanan`
--
DELIMITER $$
CREATE TRIGGER `after_pesanan_insert` AFTER INSERT ON `pesanan` FOR EACH ROW BEGIN
    INSERT INTO pengeluaran_barang (
        id_barang, jumlah, harga_total, tipe_pengeluaran, 
        tanggal_pengeluaran, keterangan, insert_by
    )
    SELECT 
        fd.id_barang,
        fd.jumlah,
        fd.jumlah * b.harga_satuan,
        'Penggunaan',
        NEW.tanggal_pesanan,
        CONCAT('Digunakan untuk pesanan ', NEW.id_pesanan),
        NEW.insert_by
    FROM layanan_detail ld
    JOIN fasilitas_detail fd ON ld.id_fasilitas = fd.id_fasilitas
    JOIN barang b ON fd.id_barang = b.id_barang
    WHERE ld.id_layanan = NEW.id_layanan;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `after_pesanan_insert_member` AFTER UPDATE ON `pesanan` FOR EACH ROW BEGIN
    DECLARE total_transaksi INT;
    DECLARE no_telepon VARCHAR(45);

    -- Hanya jalankan jika status berubah menjadi 'Selesai'
    IF NEW.status = 'Selesai' AND OLD.status != 'Selesai' THEN
        -- Ambil nomor telepon dari pelanggan
        SELECT telepon INTO no_telepon
        FROM pelanggan
        WHERE id_pelanggan = NEW.id_pelanggan;

        -- Hitung jumlah transaksi selesai berdasarkan nomor telepon hanya untuk pelanggan dengan member_status = 'aktif'
        SELECT COUNT(*) INTO total_transaksi
        FROM pesanan p
        JOIN pelanggan pl ON p.id_pelanggan = pl.id_pelanggan
        WHERE pl.telepon = no_telepon
          AND p.is_delete = 0
          AND p.status = 'Selesai'
          AND pl.member_status = 'aktif';

        -- Perbarui jumlah_transaksi hanya untuk pelanggan dengan member_status = 'aktif'
        UPDATE pelanggan
        SET jumlah_transaksi = total_transaksi
        WHERE telepon = no_telepon
          AND member_status = 'aktif';
    END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `before_pesanan_insert` BEFORE INSERT ON `pesanan` FOR EACH ROW BEGIN
    DECLARE total_dipakai INT;
    DECLARE stok_tersedia INT;
    DECLARE id_barang_item VARCHAR(255);
    DECLARE done INT DEFAULT FALSE;
    DECLARE error_msg VARCHAR(255);

    DECLARE cur CURSOR FOR
        SELECT b.id_barang, b.stok, SUM(fd.jumlah) AS total_dipakai
        FROM layanan_detail ld
        JOIN fasilitas_detail fd ON ld.id_fasilitas = fd.id_fasilitas
        JOIN barang b ON fd.id_barang = b.id_barang
        WHERE ld.id_layanan = NEW.id_layanan
        GROUP BY b.id_barang;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN cur;
    read_loop: LOOP
        FETCH cur INTO id_barang_item, stok_tersedia, total_dipakai;
        IF done THEN
            LEAVE read_loop;
        END IF;

        IF stok_tersedia < total_dipakai THEN
            SET error_msg = CONCAT('Stok barang ID ', id_barang_item, ' tidak mencukupi');
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = error_msg;
        END IF;
    END LOOP;
    CLOSE cur;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `before_pesanan_insert_discount` BEFORE INSERT ON `pesanan` FOR EACH ROW BEGIN
    DECLARE status_member ENUM('aktif', 'tidak aktif');
    DECLARE jumlah_trans INT;

    SELECT member_status, jumlah_transaksi INTO status_member, jumlah_trans
    FROM pelanggan
    WHERE id_pelanggan = NEW.id_pelanggan;

    IF status_member = 'aktif' AND (jumlah_trans <= 14 OR jumlah_trans % 15 = 0) THEN
        SET NEW.total_harga = NEW.total_harga * 0.5;
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `id_users` int(11) NOT NULL,
  `nama` varchar(45) NOT NULL,
  `username` varchar(45) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(45) NOT NULL,
  `insert_by` int(11) DEFAULT NULL,
  `update_by` int(11) DEFAULT NULL,
  `delete_by` int(11) DEFAULT NULL,
  `insert_at` datetime NOT NULL DEFAULT current_timestamp(),
  `update_at` datetime DEFAULT NULL,
  `delete_at` datetime DEFAULT NULL,
  `is_delete` tinyint(4) NOT NULL DEFAULT 0,
  `rfid_id` varchar(50) DEFAULT NULL,
  `is_default_password` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`id_users`, `nama`, `username`, `password`, `role`, `insert_by`, `update_by`, `delete_by`, `insert_at`, `update_at`, `delete_at`, `is_delete`, `rfid_id`, `is_default_password`) VALUES
(1, 'Administrator', 'admin', '$2a$10$QmR3/VGbWgQ0IwFfHWFEmuKEN1uiuCSqW63QBoL1whz3hg6VhCnv6', 'Owner', NULL, NULL, NULL, '2025-03-04 18:50:31', NULL, NULL, 0, '0249201518', 0),
(2, 'Eric', 'eric', '$2a$10$4QTDF9h636TpurO5kcJUue4GemGrZCgckcco.XBbiMi./NtDxGKgK', 'Kasir', 1, 2, NULL, '2025-03-06 14:05:15', '2025-05-31 21:01:13', NULL, 0, '12344321', 0),
(3, 'ramzy', 'ramzy', '$2a$10$tyfauwigC7CaTRyZS/uUq.nvhqPKxQOw8s7F1r9lYW7k1O84AzlWO', 'Kasir', 1, 1, 1, '2025-05-19 18:47:25', '2025-05-19 19:04:42', '2025-05-19 19:06:01', 1, '87654321', 0),
(4, 'ramzy', 'ramzyy', '$2a$10$4yLmoOI8xqd7a4N8X8nRg.6YFvlX/mXxOKX1ddXXrStoCITEMloay', 'Kasir', 1, NULL, 1, '2025-05-19 19:06:30', NULL, '2025-05-19 19:07:42', 1, '87654321', 1),
(5, 'ramzy', 'ramzyyy', '$2a$10$DvfmEZ54cO.zb6gPBp/lJevWGj1lGovbbfvngZiFMc/69x.WReKVa', 'Kasir', 1, 1, NULL, '2025-05-19 19:08:04', '2025-05-19 20:15:16', NULL, 0, '87654321', 0),
(6, 'adit', 'adit', '$2a$10$3fxxg1abRHRgMyc.9ZfnCeEEj5/k8ONUO10ebAUSTYwRjWIzVEZD.', 'Kasir', 1, NULL, NULL, '2025-05-19 20:16:31', NULL, NULL, 0, '09876543', 0),
(7, 'adit', 'aditt', '$2a$10$6s4uOUbw7.PQWHv.FM4PXOrwMz6mDdKBhYmQZQtcj29a9XZo3/4j2', 'Kasir', 1, NULL, NULL, '2025-05-19 20:17:23', NULL, NULL, 0, '34567890', 0),
(8, 'adit', 'adittt', '$2a$10$EZibYSqrEQuT50ocHkGTruKWXUzQOBIi9SSc0XCzmHnK5crYEhE2m', 'Kasir', 1, 1, NULL, '2025-05-19 20:29:08', '2025-05-19 20:30:28', NULL, 0, '10293847', 0),
(9, 'firdo', 'firdo', '$2a$10$OTSgtkNrVrw0Pal60D9tpe.nYCTfI/X8AmxylljO1ZF/Km.kJTAlW', 'Kasir', 1, 1, NULL, '2025-05-19 20:31:38', '2025-05-19 20:34:03', NULL, 0, '01928374', 0),
(10, 'Didit', 'hello Dit', '$2a$10$dwCgtSci/eF0.Np67l2PbuU3JWnDtLURzKUXDpuxfxeIuLF/qCCj2', 'Kasir', 1, NULL, 1, '2025-05-27 23:56:27', NULL, '2025-06-05 11:08:14', 1, '123321', 0);

-- --------------------------------------------------------

--
-- Stand-in struktur untuk tampilan `view_pengeluaran`
-- (Lihat di bawah untuk tampilan aktual)
--
CREATE TABLE `view_pengeluaran` (
`id` int(11)
,`deskripsi` varchar(314)
,`jumlah` decimal(15,2)
,`tanggal` date
,`sumber` varchar(11)
);

-- --------------------------------------------------------

--
-- Stand-in struktur untuk tampilan `view_ringkasan_stok`
-- (Lihat di bawah untuk tampilan aktual)
--
CREATE TABLE `view_ringkasan_stok` (
`id_barang` int(11)
,`nama_barang` varchar(45)
,`total_masuk` decimal(32,0)
,`total_keluar` decimal(32,0)
,`selisih` decimal(33,0)
);

-- --------------------------------------------------------

--
-- Struktur untuk view `view_pengeluaran`
--
DROP TABLE IF EXISTS `view_pengeluaran`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `view_pengeluaran`  AS SELECT `bo`.`id_biaya` AS `id`, `bo`.`deskripsi` AS `deskripsi`, `bo`.`jumlah_biaya` AS `jumlah`, `bo`.`tanggal_biaya` AS `tanggal`, 'Operasional' AS `sumber` FROM `biaya_operasional` AS `bo` WHERE `bo`.`is_delete` = 0union allselect `pb`.`id_pengeluaran` AS `id`,concat('Penggunaan ',`b`.`nama_barang`,' (',`pb`.`keterangan`,')') AS `deskripsi`,`pb`.`harga_total` AS `jumlah`,`pb`.`tanggal_pengeluaran` AS `tanggal`,'Barang' AS `sumber` from (`pengeluaran_barang` `pb` join `barang` `b` on(`pb`.`id_barang` = `b`.`id_barang`)) where `pb`.`is_delete` = 0 and `pb`.`tipe_pengeluaran` = 'Penggunaan'  ;

-- --------------------------------------------------------

--
-- Struktur untuk view `view_ringkasan_stok`
--
DROP TABLE IF EXISTS `view_ringkasan_stok`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `view_ringkasan_stok`  AS SELECT `b`.`id_barang` AS `id_barang`, `b`.`nama_barang` AS `nama_barang`, coalesce(sum(case when `ks`.`tipe` = 'MASUK' then `ks`.`jumlah` else 0 end),0) AS `total_masuk`, coalesce(sum(case when `ks`.`tipe` = 'KELUAR' then `ks`.`jumlah` else 0 end),0) AS `total_keluar`, coalesce(sum(case when `ks`.`tipe` = 'MASUK' then `ks`.`jumlah` else 0 end),0) - coalesce(sum(case when `ks`.`tipe` = 'KELUAR' then `ks`.`jumlah` else 0 end),0) AS `selisih` FROM (`barang` `b` left join `kartu_stok` `ks` on(`b`.`id_barang` = `ks`.`id_barang`)) GROUP BY `b`.`id_barang`, `b`.`nama_barang` ;

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `absensi`
--
ALTER TABLE `absensi`
  ADD PRIMARY KEY (`id_absensi`),
  ADD KEY `absensi_ibfk_1` (`id_karyawan`);

--
-- Indeks untuk tabel `barang`
--
ALTER TABLE `barang`
  ADD PRIMARY KEY (`id_barang`),
  ADD KEY `barang_1_idx` (`insert_by`),
  ADD KEY `barang_2_idx` (`update_by`),
  ADD KEY `barang_3_idx` (`delete_by`);

--
-- Indeks untuk tabel `biaya_operasional`
--
ALTER TABLE `biaya_operasional`
  ADD PRIMARY KEY (`id_biaya`),
  ADD KEY `insert_by` (`insert_by`);

--
-- Indeks untuk tabel `detail_opname_stok`
--
ALTER TABLE `detail_opname_stok`
  ADD PRIMARY KEY (`id_detail_opname`),
  ADD KEY `id_opname` (`id_opname`),
  ADD KEY `id_barang` (`id_barang`);

--
-- Indeks untuk tabel `fasilitas`
--
ALTER TABLE `fasilitas`
  ADD PRIMARY KEY (`id_fasilitas`),
  ADD KEY `layanan_1_idx` (`insert_by`),
  ADD KEY `layanan_2_idx` (`update_by`),
  ADD KEY `layanan_3_idx` (`delete_by`);

--
-- Indeks untuk tabel `fasilitas_detail`
--
ALTER TABLE `fasilitas_detail`
  ADD KEY `fasilitas_detail_1_idx` (`id_fasilitas`),
  ADD KEY `fasilitas_detail_2_idx` (`id_barang`);

--
-- Indeks untuk tabel `kartu_stok`
--
ALTER TABLE `kartu_stok`
  ADD PRIMARY KEY (`id_kartu_stok`),
  ADD KEY `id_barang` (`id_barang`);

--
-- Indeks untuk tabel `karyawan`
--
ALTER TABLE `karyawan`
  ADD PRIMARY KEY (`id_karyawan`),
  ADD KEY `karyawan_1_idx` (`insert_by`),
  ADD KEY `karyawan_2_idx` (`update_by`),
  ADD KEY `karyawan_3_idx` (`delete_by`);

--
-- Indeks untuk tabel `layanan`
--
ALTER TABLE `layanan`
  ADD PRIMARY KEY (`id_layanan`),
  ADD KEY `layanan_1_idx` (`insert_by`),
  ADD KEY `layanan_2_idx` (`update_by`),
  ADD KEY `layanan_3_idx` (`delete_by`);

--
-- Indeks untuk tabel `layanan_detail`
--
ALTER TABLE `layanan_detail`
  ADD KEY `layanan_detail_1_idx` (`id_layanan`),
  ADD KEY `layanan_detail_2_idx` (`id_fasilitas`);

--
-- Indeks untuk tabel `log_barang_masuk`
--
ALTER TABLE `log_barang_masuk`
  ADD PRIMARY KEY (`id_log`),
  ADD KEY `id_barang` (`id_barang`),
  ADD KEY `id_users` (`id_users`);

--
-- Indeks untuk tabel `opname_stok`
--
ALTER TABLE `opname_stok`
  ADD PRIMARY KEY (`id_opname`),
  ADD KEY `insert_by` (`insert_by`);

--
-- Indeks untuk tabel `pelanggan`
--
ALTER TABLE `pelanggan`
  ADD PRIMARY KEY (`id_pelanggan`),
  ADD KEY `users_1_idx` (`insert_by`),
  ADD KEY `users_2_idx` (`update_by`),
  ADD KEY `users_3_idx` (`delete_by`),
  ADD KEY `idx_pelanggan_telepon` (`telepon`);

--
-- Indeks untuk tabel `pengeluaran_barang`
--
ALTER TABLE `pengeluaran_barang`
  ADD PRIMARY KEY (`id_pengeluaran`),
  ADD KEY `id_barang` (`id_barang`),
  ADD KEY `insert_by` (`insert_by`);

--
-- Indeks untuk tabel `pesanan`
--
ALTER TABLE `pesanan`
  ADD PRIMARY KEY (`id_pesanan`),
  ADD KEY `pesanan_1_idx` (`insert_by`),
  ADD KEY `pesanan_2_idx` (`update_by`),
  ADD KEY `pesanan_3_idx` (`delete_by`),
  ADD KEY `pesanan_4_idx` (`id_pelanggan`),
  ADD KEY `pesanan_5_idx` (`id_layanan`),
  ADD KEY `idx_pesanan_id_pelanggan` (`id_pelanggan`);

--
-- Indeks untuk tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id_users`),
  ADD KEY `users_1_idx` (`insert_by`),
  ADD KEY `users_2_idx` (`update_by`),
  ADD KEY `users_3_idx` (`delete_by`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `absensi`
--
ALTER TABLE `absensi`
  MODIFY `id_absensi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=105;

--
-- AUTO_INCREMENT untuk tabel `barang`
--
ALTER TABLE `barang`
  MODIFY `id_barang` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT untuk tabel `biaya_operasional`
--
ALTER TABLE `biaya_operasional`
  MODIFY `id_biaya` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT untuk tabel `detail_opname_stok`
--
ALTER TABLE `detail_opname_stok`
  MODIFY `id_detail_opname` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT untuk tabel `fasilitas`
--
ALTER TABLE `fasilitas`
  MODIFY `id_fasilitas` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT untuk tabel `kartu_stok`
--
ALTER TABLE `kartu_stok`
  MODIFY `id_kartu_stok` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=140;

--
-- AUTO_INCREMENT untuk tabel `karyawan`
--
ALTER TABLE `karyawan`
  MODIFY `id_karyawan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT untuk tabel `layanan`
--
ALTER TABLE `layanan`
  MODIFY `id_layanan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT untuk tabel `log_barang_masuk`
--
ALTER TABLE `log_barang_masuk`
  MODIFY `id_log` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT untuk tabel `opname_stok`
--
ALTER TABLE `opname_stok`
  MODIFY `id_opname` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT untuk tabel `pengeluaran_barang`
--
ALTER TABLE `pengeluaran_barang`
  MODIFY `id_pengeluaran` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=202;

--
-- AUTO_INCREMENT untuk tabel `users`
--
ALTER TABLE `users`
  MODIFY `id_users` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `barang`
--
ALTER TABLE `barang`
  ADD CONSTRAINT `karyawan_10` FOREIGN KEY (`insert_by`) REFERENCES `users` (`id_users`),
  ADD CONSTRAINT `karyawan_20` FOREIGN KEY (`update_by`) REFERENCES `users` (`id_users`),
  ADD CONSTRAINT `karyawan_30` FOREIGN KEY (`delete_by`) REFERENCES `users` (`id_users`);

--
-- Ketidakleluasaan untuk tabel `biaya_operasional`
--
ALTER TABLE `biaya_operasional`
  ADD CONSTRAINT `biaya_operasional_ibfk_1` FOREIGN KEY (`insert_by`) REFERENCES `users` (`id_users`);

--
-- Ketidakleluasaan untuk tabel `detail_opname_stok`
--
ALTER TABLE `detail_opname_stok`
  ADD CONSTRAINT `detail_opname_stok_ibfk_1` FOREIGN KEY (`id_opname`) REFERENCES `opname_stok` (`id_opname`) ON DELETE CASCADE,
  ADD CONSTRAINT `detail_opname_stok_ibfk_2` FOREIGN KEY (`id_barang`) REFERENCES `barang` (`id_barang`) ON DELETE CASCADE;

--
-- Ketidakleluasaan untuk tabel `fasilitas`
--
ALTER TABLE `fasilitas`
  ADD CONSTRAINT `layanan_10` FOREIGN KEY (`insert_by`) REFERENCES `users` (`id_users`),
  ADD CONSTRAINT `layanan_20` FOREIGN KEY (`update_by`) REFERENCES `users` (`id_users`),
  ADD CONSTRAINT `layanan_30` FOREIGN KEY (`delete_by`) REFERENCES `users` (`id_users`);

--
-- Ketidakleluasaan untuk tabel `fasilitas_detail`
--
ALTER TABLE `fasilitas_detail`
  ADD CONSTRAINT `fasilitas_detail_1` FOREIGN KEY (`id_fasilitas`) REFERENCES `fasilitas` (`id_fasilitas`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fasilitas_detail_2` FOREIGN KEY (`id_barang`) REFERENCES `barang` (`id_barang`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `kartu_stok`
--
ALTER TABLE `kartu_stok`
  ADD CONSTRAINT `kartu_stok_ibfk_1` FOREIGN KEY (`id_barang`) REFERENCES `barang` (`id_barang`);

--
-- Ketidakleluasaan untuk tabel `karyawan`
--
ALTER TABLE `karyawan`
  ADD CONSTRAINT `karyawan_1` FOREIGN KEY (`insert_by`) REFERENCES `users` (`id_users`),
  ADD CONSTRAINT `karyawan_2` FOREIGN KEY (`update_by`) REFERENCES `users` (`id_users`),
  ADD CONSTRAINT `karyawan_3` FOREIGN KEY (`delete_by`) REFERENCES `users` (`id_users`);

--
-- Ketidakleluasaan untuk tabel `layanan`
--
ALTER TABLE `layanan`
  ADD CONSTRAINT `layanan_1` FOREIGN KEY (`insert_by`) REFERENCES `users` (`id_users`),
  ADD CONSTRAINT `layanan_2` FOREIGN KEY (`update_by`) REFERENCES `users` (`id_users`),
  ADD CONSTRAINT `layanan_3` FOREIGN KEY (`delete_by`) REFERENCES `users` (`id_users`);

--
-- Ketidakleluasaan untuk tabel `layanan_detail`
--
ALTER TABLE `layanan_detail`
  ADD CONSTRAINT `layanan_detail_1` FOREIGN KEY (`id_layanan`) REFERENCES `layanan` (`id_layanan`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `layanan_detail_2` FOREIGN KEY (`id_fasilitas`) REFERENCES `fasilitas` (`id_fasilitas`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `log_barang_masuk`
--
ALTER TABLE `log_barang_masuk`
  ADD CONSTRAINT `log_barang_masuk_ibfk_1` FOREIGN KEY (`id_barang`) REFERENCES `barang` (`id_barang`) ON DELETE CASCADE,
  ADD CONSTRAINT `log_barang_masuk_ibfk_2` FOREIGN KEY (`id_users`) REFERENCES `users` (`id_users`) ON DELETE CASCADE;

--
-- Ketidakleluasaan untuk tabel `opname_stok`
--
ALTER TABLE `opname_stok`
  ADD CONSTRAINT `opname_stok_ibfk_1` FOREIGN KEY (`insert_by`) REFERENCES `users` (`id_users`);

--
-- Ketidakleluasaan untuk tabel `pelanggan`
--
ALTER TABLE `pelanggan`
  ADD CONSTRAINT `pelanggan_1` FOREIGN KEY (`insert_by`) REFERENCES `users` (`id_users`),
  ADD CONSTRAINT `pelanggan_2` FOREIGN KEY (`update_by`) REFERENCES `users` (`id_users`),
  ADD CONSTRAINT `pelanggan_3` FOREIGN KEY (`delete_by`) REFERENCES `users` (`id_users`);

--
-- Ketidakleluasaan untuk tabel `pengeluaran_barang`
--
ALTER TABLE `pengeluaran_barang`
  ADD CONSTRAINT `pengeluaran_barang_ibfk_1` FOREIGN KEY (`id_barang`) REFERENCES `barang` (`id_barang`) ON DELETE CASCADE,
  ADD CONSTRAINT `pengeluaran_barang_ibfk_2` FOREIGN KEY (`insert_by`) REFERENCES `users` (`id_users`);

--
-- Ketidakleluasaan untuk tabel `pesanan`
--
ALTER TABLE `pesanan`
  ADD CONSTRAINT `pesanan_1` FOREIGN KEY (`insert_by`) REFERENCES `users` (`id_users`),
  ADD CONSTRAINT `pesanan_2` FOREIGN KEY (`update_by`) REFERENCES `users` (`id_users`),
  ADD CONSTRAINT `pesanan_3` FOREIGN KEY (`delete_by`) REFERENCES `users` (`id_users`),
  ADD CONSTRAINT `pesanan_4` FOREIGN KEY (`id_pelanggan`) REFERENCES `pelanggan` (`id_pelanggan`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `pesanan_5` FOREIGN KEY (`id_layanan`) REFERENCES `layanan` (`id_layanan`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `users_1` FOREIGN KEY (`insert_by`) REFERENCES `users` (`id_users`),
  ADD CONSTRAINT `users_2` FOREIGN KEY (`update_by`) REFERENCES `users` (`id_users`),
  ADD CONSTRAINT `users_3` FOREIGN KEY (`delete_by`) REFERENCES `users` (`id_users`);

DELIMITER $$
--
-- Event
--
CREATE DEFINER=`root`@`localhost` EVENT `deactivate_inactive_members` ON SCHEDULE EVERY 1 DAY STARTS '2025-06-03 23:19:43' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN
    UPDATE pelanggan
    SET member_status = 'tidak aktif'
    WHERE member_status = 'aktif'
      AND id_pelanggan NOT IN (
          SELECT id_pelanggan
          FROM pesanan
          WHERE status = 'Selesai'
            AND tanggal_pesanan >= NOW() - INTERVAL 7 DAY
      );
END$$

CREATE DEFINER=`root`@`localhost` EVENT `reset_no_antrian_daily` ON SCHEDULE EVERY 1 DAY STARTS '2025-06-05 00:00:00' ON COMPLETION PRESERVE ENABLE DO BEGIN
    UPDATE pesanan
    SET no_antrian = NULL
    WHERE tanggal_pesanan < CURRENT_DATE
      AND no_antrian IS NOT NULL;
END$$

DELIMITER ;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
