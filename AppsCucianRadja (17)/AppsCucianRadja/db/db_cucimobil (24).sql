-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 04 Jun 2025 pada 15.20
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
(103, 1, '2025-05-31 21:17:21', '2025-05-31 21:18:37', 'HADIR', 'Terlambat Masuk', 0, '2025-05-31', 0.02);

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
(1, 'Sabun Cuci Mobil', 'sachet', 9651, 1500.15, 1, 1, 1, '2025-03-07 17:27:36', '2025-06-02 16:31:25', '2025-06-02 16:32:03', 1, 15.00),
(2, 'Shampo Mobil', 'sachet', 9250, 2500.00, 1, 1, NULL, '2025-03-07 17:27:36', '2025-03-17 13:09:16', NULL, 0, 100.00),
(3, 'Lap Microfiber', 'pcs', 266, 15000.00, 1, NULL, NULL, '2025-03-07 17:27:36', NULL, NULL, 0, NULL),
(4, 'Wax Premium', 'botol', 14, 100000.00, 1, NULL, NULL, '2025-03-07 17:27:36', NULL, NULL, 0, NULL),
(5, 'Semir Ban', 'sachet', 799, 100.00, 1, NULL, 1, '2025-03-07 17:27:36', NULL, '2025-05-23 14:48:59', 1, 15.00),
(6, 'Wax Premium', 'Botol', 50, 10.00, 1, NULL, 1, '2025-05-26 00:13:58', NULL, '2025-05-26 00:14:31', 1, NULL),
(7, 'Wax Premium', 'botol', 50, 50000.00, 1, NULL, NULL, '2025-05-26 00:15:15', NULL, NULL, 0, NULL),
(8, 'kain', 'pcs', 10, 10000.00, 1, NULL, NULL, '2025-05-26 09:43:27', NULL, NULL, 0, NULL),
(9, 'Wax Premium', 'botol', 50, 100000.00, 1, NULL, NULL, '2025-05-26 09:53:49', NULL, NULL, 0, NULL);

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
(1, 'Sewa Tempat', 3000000.00, '2025-05-25', 1, '2025-05-23 13:41:07', 1),
(2, 'Tagihan Listrik', 1500000.00, '2025-05-23', 1, '2025-05-23 14:19:58', 1),
(3, 'Gaji Karyawan', 5000000.00, '2025-05-23', 1, '2025-05-23 14:19:58', 1),
(4, 'tes', 123.46, '2025-05-25', 1, '2025-05-25 23:47:22', 1),
(5, 'Gaji Karyawan', 7000000.00, '2025-06-02', 1, '2025-06-02 17:12:12', 0);

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
(1, 'Cuci Salju', 1, NULL, NULL, '2025-03-07 17:27:56', NULL, NULL, 0),
(2, 'Cuci Interior', 1, NULL, NULL, '2025-03-07 17:27:56', NULL, NULL, 0),
(3, 'Poles Body', 1, NULL, NULL, '2025-03-07 17:27:56', NULL, NULL, 0),
(4, 'Poles Kaca', 1, NULL, NULL, '2025-03-07 17:27:56', NULL, NULL, 0),
(5, 'Pembersihan Mesin', 1, NULL, NULL, '2025-03-07 17:27:56', NULL, NULL, 0);

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
(1, 1, 7),
(1, 2, 50),
(2, 3, 2),
(3, 4, 1),
(4, 4, 1),
(5, 2, 20);

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
(1, 6, '2025-05-26 00:13:58', 'MASUK', 50, 'Dari log_barang_masuk'),
(2, 7, '2025-05-26 00:15:15', 'MASUK', 50, 'Dari log_barang_masuk'),
(3, 8, '2025-05-26 09:43:27', 'MASUK', 10, 'Dari log_barang_masuk'),
(4, 9, '2025-05-26 09:53:49', 'MASUK', 50, 'Dari log_barang_masuk'),
(5, 1, '2025-06-03 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060020'),
(6, 2, '2025-06-03 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060020'),
(7, 1, '2025-06-03 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060020'),
(8, 2, '2025-06-03 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060020'),
(9, 1, '2025-06-03 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060021'),
(10, 2, '2025-06-03 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060021'),
(11, 1, '2025-06-03 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060021'),
(12, 2, '2025-06-03 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060021'),
(13, 1, '2025-06-03 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060022'),
(14, 2, '2025-06-03 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060022'),
(15, 1, '2025-06-03 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060023'),
(16, 2, '2025-06-03 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060023'),
(17, 1, '2025-06-03 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060024'),
(18, 2, '2025-06-03 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060024'),
(19, 1, '2025-06-03 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060025'),
(20, 2, '2025-06-03 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060025'),
(21, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060026'),
(22, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060026'),
(23, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060001'),
(24, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060001'),
(25, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060002'),
(26, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060002'),
(27, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060003'),
(28, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060003'),
(29, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060004'),
(30, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060004'),
(31, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060005'),
(32, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060005'),
(33, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060006'),
(34, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060006'),
(35, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060007'),
(36, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060007'),
(37, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060008'),
(38, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060008'),
(39, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060009'),
(40, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060009'),
(41, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060010'),
(42, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060010'),
(43, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060011'),
(44, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060011'),
(45, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060012'),
(46, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060012'),
(47, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060013'),
(48, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060013'),
(49, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060014'),
(50, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060014'),
(51, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060015'),
(52, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060015'),
(53, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060016'),
(54, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060016'),
(55, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060017'),
(56, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060017'),
(57, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060018'),
(58, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060018'),
(59, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060019'),
(60, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060019'),
(61, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060020'),
(62, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060020'),
(63, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060021'),
(64, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060021'),
(65, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060022'),
(66, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060022'),
(67, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060023'),
(68, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060023'),
(69, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060024'),
(70, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060024'),
(71, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060025'),
(72, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060025'),
(73, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060026'),
(74, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060026'),
(75, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060027'),
(76, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060027'),
(77, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060028'),
(78, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060028'),
(79, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060029'),
(80, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060029'),
(81, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060030'),
(82, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060030'),
(83, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060031'),
(84, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060031'),
(85, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060032'),
(86, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060032'),
(87, 3, '2025-06-04 00:00:00', 'KELUAR', 2, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060032'),
(88, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060033'),
(89, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060033'),
(90, 3, '2025-06-04 00:00:00', 'KELUAR', 2, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060033'),
(91, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060034'),
(92, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060034'),
(93, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060035'),
(94, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060035'),
(95, 3, '2025-06-04 00:00:00', 'KELUAR', 2, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060035'),
(96, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060036'),
(97, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060036'),
(98, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060037'),
(99, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060037'),
(100, 3, '2025-06-04 00:00:00', 'KELUAR', 2, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060037'),
(101, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060038'),
(102, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060038'),
(103, 3, '2025-06-04 00:00:00', 'KELUAR', 2, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060038'),
(104, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060039'),
(105, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060039'),
(106, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060040'),
(107, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060040'),
(108, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060041'),
(109, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060041'),
(110, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060042'),
(111, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060042'),
(112, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060043'),
(113, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060043'),
(114, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060044'),
(115, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060044'),
(116, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060045'),
(117, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060045'),
(118, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060046'),
(119, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060046'),
(120, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060047'),
(121, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060047'),
(122, 1, '2025-06-04 00:00:00', 'KELUAR', 7, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060048'),
(123, 2, '2025-06-04 00:00:00', 'KELUAR', 50, 'Pengeluaran tipe: Penggunaan, Keterangan: Digunakan untuk pesanan 25060048');

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
(4, 'Rudi Hartono', '084567890123', 'Jl. Thamrin No.5, Tangerang', '123', 1, 1, NULL, '2025-03-07 17:28:14', '2025-05-05 09:40:30', NULL, 0);

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
(1, 6, 'Wax Premium', 'Botol', 50, 10.00, 1, '2025-05-26 00:13:58'),
(2, 7, 'Wax Premium', 'botol', 50, 50000.00, 1, '2025-05-26 00:15:15'),
(3, 8, 'kain', 'pcs', 10, 10000.00, 1, '2025-05-26 09:43:27'),
(4, 9, 'Wax Premium', 'botol', 50, 100000.00, 1, '2025-05-26 09:53:49');

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
('CR20250604001', 'tes', '1234567890', 'asdasdasd', 'aktif', 1, 1, NULL, '2025-06-04 00:08:51', '2025-06-04 00:23:17', NULL, 0, 23),
('CR20250604002', 'lala', '456456546546', 'asdas', 'tidak aktif', 1, NULL, NULL, '2025-06-04 00:19:04', NULL, NULL, 0, 0),
('CR20250604003', 'asdas', '1231241242342', 'asd', 'tidak aktif', 1, NULL, NULL, '2025-06-04 01:17:44', NULL, NULL, 0, 0),
('CR20250604004', 'asda', '123213', 'asd', 'tidak aktif', 1, NULL, NULL, '2025-06-04 01:46:51', NULL, NULL, 0, 0),
('CR20250604005', 'asdasd', '212142343242', 'asd', 'tidak aktif', 1, NULL, NULL, '2025-06-04 19:01:47', NULL, NULL, 0, 0),
('CR20250604006', 'asd', '12423432', 'asdas', 'tidak aktif', 1, NULL, NULL, '2025-06-04 19:02:21', NULL, NULL, 0, 0),
('CR20250604007', 'aasd', '32435435353', 'asd', 'tidak aktif', 1, NULL, NULL, '2025-06-04 19:02:40', NULL, NULL, 0, 0),
('CR20250604008', 'asda', '32423424234', 'sad', 'tidak aktif', 1, NULL, NULL, '2025-06-04 19:03:31', NULL, NULL, 0, 0),
('CR20250604009', 'asda', '1423423423', 'asd', 'tidak aktif', 1, NULL, NULL, '2025-06-04 19:19:40', NULL, NULL, 0, 0),
('CR20250604010', 'asdas', '324324324', 'asd', 'tidak aktif', 1, NULL, NULL, '2025-06-04 19:20:09', NULL, NULL, 0, 0),
('CR20250604011', 'asdasd', '2423432432', 'asd', 'tidak aktif', 1, NULL, NULL, '2025-06-04 19:24:08', NULL, NULL, 0, 0),
('CR20250604012', 'asdas23', '3243242', 'asdasd', 'tidak aktif', 1, NULL, NULL, '2025-06-04 19:24:24', NULL, NULL, 0, 0),
('CR20250604013', 'asdasd', '34234', 'asdasda', 'tidak aktif', 1, NULL, NULL, '2025-06-04 19:27:24', NULL, NULL, 0, 0),
('CR20250604014', 'asdasd', '23423432', 'asdasdasd', 'tidak aktif', 1, NULL, NULL, '2025-06-04 19:27:42', NULL, NULL, 0, 0),
('CR20250604015', 'asdasd', '32423423', 'sadasd', 'tidak aktif', 1, NULL, NULL, '2025-06-04 19:29:42', NULL, NULL, 0, 0),
('CR20250604016', 'asdasd', '24234324', 'asdasd', 'tidak aktif', 1, NULL, NULL, '2025-06-04 19:38:46', NULL, NULL, 0, 0),
('CR20250604017', 'asdasd', '234234', 'sadas', 'tidak aktif', 1, NULL, NULL, '2025-06-04 19:44:00', NULL, NULL, 0, 0),
('CR20250604018', 'asd', '24234', 'asd', 'tidak aktif', 1, NULL, NULL, '2025-06-04 19:44:13', NULL, NULL, 0, 0),
('CR20250604019', 'asdasd', '23432423', 'asdas', 'tidak aktif', 1, NULL, NULL, '2025-06-04 19:46:12', NULL, NULL, 0, 0),
('CR20250604020', 'asd', '21123', 'asdasd', 'tidak aktif', 1, NULL, NULL, '2025-06-04 19:48:10', NULL, NULL, 0, 0);

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
(4, 1, 50000, 5000000.00, 'Pembelian', '2025-05-23', 'Pembelian Sabun Cuci Mobil 50 liter', 1, '2025-05-23 13:37:15', 0),
(5, 2, 50, 1250000.00, 'Pembelian', '2025-05-23', 'Pembelian Shampo Mobil 50 liter', 1, '2025-05-23 13:37:15', 0),
(6, 5, 10000, 600000.00, 'Pembelian', '2025-05-23', 'Pembelian Semir Ban 10 liter', 1, '2025-05-23 13:37:15', 0),
(7, 1, 3333, 5000000.00, 'Pembelian', '2025-05-23', 'Pembelian Sabun Cuci Mobil 50 liter (3333 sachet)', 1, '2025-05-23 14:18:16', 0),
(8, 2, 500, 1250000.00, 'Pembelian', '2025-05-23', 'Pembelian Shampo Mobil 50 liter (500 sachet)', 1, '2025-05-23 14:18:16', 0),
(9, 5, 666, 66600.00, 'Pembelian', '2025-05-23', 'Pembelian Semir Ban 10 liter (666 sachet)', 1, '2025-05-23 14:18:16', 0),
(13, 1, 7, 10501.05, 'Penggunaan', '2025-06-03', 'Digunakan untuk pesanan 25060020', 1, '2025-06-03 03:40:52', 0),
(14, 2, 50, 125000.00, 'Penggunaan', '2025-06-03', 'Digunakan untuk pesanan 25060020', 1, '2025-06-03 03:40:52', 0),
(16, 1, 7, 10501.05, 'Penggunaan', '2025-06-03', 'Digunakan untuk pesanan 25060020', 1, '2025-06-03 03:40:52', 0),
(17, 2, 50, 125000.00, 'Penggunaan', '2025-06-03', 'Digunakan untuk pesanan 25060020', 1, '2025-06-03 03:40:52', 0),
(19, 1, 7, 10501.05, 'Penggunaan', '2025-06-03', 'Digunakan untuk pesanan 25060021', 1, '2025-06-03 03:41:13', 0),
(20, 2, 50, 125000.00, 'Penggunaan', '2025-06-03', 'Digunakan untuk pesanan 25060021', 1, '2025-06-03 03:41:13', 0),
(22, 1, 7, 10501.05, 'Penggunaan', '2025-06-03', 'Digunakan untuk pesanan 25060021', 1, '2025-06-03 03:41:13', 0),
(23, 2, 50, 125000.00, 'Penggunaan', '2025-06-03', 'Digunakan untuk pesanan 25060021', 1, '2025-06-03 03:41:13', 0),
(25, 1, 7, 10501.05, 'Penggunaan', '2025-06-03', 'Digunakan untuk pesanan 25060022', 1, '2025-06-03 23:02:26', 0),
(26, 2, 50, 125000.00, 'Penggunaan', '2025-06-03', 'Digunakan untuk pesanan 25060022', 1, '2025-06-03 23:02:26', 0),
(28, 1, 7, 10501.05, 'Penggunaan', '2025-06-03', 'Digunakan untuk pesanan 25060023', 1, '2025-06-03 23:03:33', 0),
(29, 2, 50, 125000.00, 'Penggunaan', '2025-06-03', 'Digunakan untuk pesanan 25060023', 1, '2025-06-03 23:03:33', 0),
(31, 1, 7, 10501.05, 'Penggunaan', '2025-06-03', 'Digunakan untuk pesanan 25060024', 1, '2025-06-03 23:11:07', 0),
(32, 2, 50, 125000.00, 'Penggunaan', '2025-06-03', 'Digunakan untuk pesanan 25060024', 1, '2025-06-03 23:11:07', 0),
(34, 1, 7, 10501.05, 'Penggunaan', '2025-06-03', 'Digunakan untuk pesanan 25060025', 1, '2025-06-03 23:14:59', 0),
(35, 2, 50, 125000.00, 'Penggunaan', '2025-06-03', 'Digunakan untuk pesanan 25060025', 1, '2025-06-03 23:14:59', 0),
(37, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060026', 1, '2025-06-04 00:02:48', 0),
(38, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060026', 1, '2025-06-04 00:02:48', 0),
(40, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060001', 1, '2025-06-04 00:08:51', 0),
(41, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060001', 1, '2025-06-04 00:08:51', 0),
(43, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060002', 1, '2025-06-04 00:09:39', 0),
(44, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060002', 1, '2025-06-04 00:09:39', 0),
(46, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060003', 1, '2025-06-04 00:19:46', 0),
(47, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060003', 1, '2025-06-04 00:19:46', 0),
(49, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060004', 1, '2025-06-04 00:21:30', 0),
(50, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060004', 1, '2025-06-04 00:21:30', 0),
(52, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060005', 1, '2025-06-04 00:23:32', 0),
(53, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060005', 1, '2025-06-04 00:23:32', 0),
(55, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060006', 1, '2025-06-04 00:24:32', 0),
(56, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060006', 1, '2025-06-04 00:24:32', 0),
(58, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060007', 1, '2025-06-04 00:24:53', 0),
(59, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060007', 1, '2025-06-04 00:24:53', 0),
(61, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060008', 1, '2025-06-04 00:34:14', 0),
(62, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060008', 1, '2025-06-04 00:34:14', 0),
(64, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060009', 1, '2025-06-04 00:34:47', 0),
(65, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060009', 1, '2025-06-04 00:34:47', 0),
(67, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060010', 1, '2025-06-04 00:35:35', 0),
(68, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060010', 1, '2025-06-04 00:35:35', 0),
(70, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060011', 1, '2025-06-04 00:36:16', 0),
(71, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060011', 1, '2025-06-04 00:36:16', 0),
(73, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060012', 1, '2025-06-04 00:40:08', 0),
(74, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060012', 1, '2025-06-04 00:40:08', 0),
(76, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060013', 1, '2025-06-04 00:41:50', 0),
(77, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060013', 1, '2025-06-04 00:41:50', 0),
(79, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060014', 1, '2025-06-04 00:58:55', 0),
(80, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060014', 1, '2025-06-04 00:58:55', 0),
(82, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060015', 1, '2025-06-04 01:00:07', 0),
(83, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060015', 1, '2025-06-04 01:00:07', 0),
(85, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060016', 1, '2025-06-04 01:01:21', 0),
(86, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060016', 1, '2025-06-04 01:01:21', 0),
(88, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060017', 1, '2025-06-04 01:01:53', 0),
(89, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060017', 1, '2025-06-04 01:01:53', 0),
(91, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060018', 1, '2025-06-04 01:01:53', 0),
(92, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060018', 1, '2025-06-04 01:01:53', 0),
(94, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060019', 1, '2025-06-04 01:02:19', 0),
(95, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060019', 1, '2025-06-04 01:02:19', 0),
(97, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060020', 1, '2025-06-04 01:02:46', 0),
(98, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060020', 1, '2025-06-04 01:02:46', 0),
(100, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060021', 1, '2025-06-04 01:03:12', 0),
(101, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060021', 1, '2025-06-04 01:03:12', 0),
(103, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060022', 1, '2025-06-04 01:03:40', 0),
(104, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060022', 1, '2025-06-04 01:03:40', 0),
(106, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060023', 1, '2025-06-04 01:04:11', 0),
(107, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060023', 1, '2025-06-04 01:04:11', 0),
(109, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060024', 1, '2025-06-04 01:07:12', 0),
(110, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060024', 1, '2025-06-04 01:07:12', 0),
(112, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060025', 1, '2025-06-04 01:17:44', 0),
(113, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060025', 1, '2025-06-04 01:17:44', 0),
(115, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060026', 1, '2025-06-04 01:23:12', 0),
(116, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060026', 1, '2025-06-04 01:23:12', 0),
(118, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060027', 1, '2025-06-04 01:23:55', 0),
(119, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060027', 1, '2025-06-04 01:23:55', 0),
(121, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060028', 1, '2025-06-04 01:25:17', 0),
(122, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060028', 1, '2025-06-04 01:25:17', 0),
(124, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060029', 1, '2025-06-04 01:26:12', 0),
(125, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060029', 1, '2025-06-04 01:26:12', 0),
(127, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060030', 1, '2025-06-04 01:26:39', 0),
(128, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060030', 1, '2025-06-04 01:26:39', 0),
(130, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060031', 1, '2025-06-04 01:38:17', 0),
(131, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060031', 1, '2025-06-04 01:38:17', 0),
(133, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060032', 1, '2025-06-04 01:39:16', 0),
(134, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060032', 1, '2025-06-04 01:39:16', 0),
(135, 3, 2, 30000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060032', 1, '2025-06-04 01:39:16', 0),
(136, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060033', 1, '2025-06-04 01:46:51', 0),
(137, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060033', 1, '2025-06-04 01:46:51', 0),
(138, 3, 2, 30000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060033', 1, '2025-06-04 01:46:51', 0),
(139, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060034', 1, '2025-06-04 19:01:47', 0),
(140, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060034', 1, '2025-06-04 19:01:47', 0),
(142, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060035', 1, '2025-06-04 19:02:21', 0),
(143, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060035', 1, '2025-06-04 19:02:21', 0),
(144, 3, 2, 30000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060035', 1, '2025-06-04 19:02:21', 0),
(145, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060036', 1, '2025-06-04 19:03:31', 0),
(146, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060036', 1, '2025-06-04 19:03:31', 0),
(148, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060037', 1, '2025-06-04 19:19:40', 0),
(149, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060037', 1, '2025-06-04 19:19:40', 0),
(150, 3, 2, 30000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060037', 1, '2025-06-04 19:19:40', 0),
(151, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060038', 1, '2025-06-04 19:20:09', 0),
(152, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060038', 1, '2025-06-04 19:20:09', 0),
(153, 3, 2, 30000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060038', 1, '2025-06-04 19:20:09', 0),
(154, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060039', 1, '2025-06-04 19:24:08', 0),
(155, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060039', 1, '2025-06-04 19:24:08', 0),
(157, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060040', 1, '2025-06-04 19:24:24', 0),
(158, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060040', 1, '2025-06-04 19:24:24', 0),
(160, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060041', 1, '2025-06-04 19:27:24', 0),
(161, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060041', 1, '2025-06-04 19:27:24', 0),
(163, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060042', 1, '2025-06-04 19:27:42', 0),
(164, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060042', 1, '2025-06-04 19:27:42', 0),
(166, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060043', 1, '2025-06-04 19:29:42', 0),
(167, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060043', 1, '2025-06-04 19:29:42', 0),
(169, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060044', 1, '2025-06-04 19:38:46', 0),
(170, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060044', 1, '2025-06-04 19:38:46', 0),
(172, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060045', 1, '2025-06-04 19:44:00', 0),
(173, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060045', 1, '2025-06-04 19:44:00', 0),
(175, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060046', 1, '2025-06-04 19:44:13', 0),
(176, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060046', 1, '2025-06-04 19:44:13', 0),
(178, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060047', 1, '2025-06-04 19:46:12', 0),
(179, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060047', 1, '2025-06-04 19:46:12', 0),
(181, 1, 7, 10501.05, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060048', 1, '2025-06-04 19:48:10', 0),
(182, 2, 50, 125000.00, 'Penggunaan', '2025-06-04', 'Digunakan untuk pesanan 25060048', 1, '2025-06-04 19:48:10', 0);

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
('25060001', '2025-06-04', 'asd', 'asdas', 35000.00, 'Selesai', 'CR20250604001', 1, 5, 1, 1, NULL, '2025-06-04 00:08:51', '2025-06-04 00:09:01', NULL, 0),
('25060002', '2025-06-04', 'asd', 'asdasd', 35000.00, 'Menunggu Antrian', 'CR20250604001', 1, 6, 1, NULL, 1, '2025-06-04 00:09:39', NULL, '2025-06-04 19:29:21', 1),
('25060003', '2025-06-04', 'zxcz', 'zxczx', 17500.00, 'Menunggu Antrian', 'CR20250604001', 1, 7, 1, NULL, 1, '2025-06-04 00:19:46', NULL, '2025-06-04 19:29:23', 1),
('25060004', '2025-06-04', 'asd', 'asd', 35000.00, 'Menunggu Antrian', 'CR20250604001', 1, 8, 1, NULL, 1, '2025-06-04 00:21:30', NULL, '2025-06-04 19:29:26', 1),
('25060005', '2025-06-04', 'asdas', 'asd', 17500.00, 'Selesai', 'CR20250604001', 1, 12, 1, 1, NULL, '2025-06-04 00:23:32', '2025-06-04 00:24:07', NULL, 0),
('25060006', '2025-06-04', 'asd', 'asd', 17500.00, 'Menunggu Antrian', 'CR20250604001', 1, 13, 1, NULL, 1, '2025-06-04 00:24:32', NULL, '2025-06-04 19:29:28', 1),
('25060007', '2025-06-04', 'asda', 'asd', 17500.00, 'Menunggu Antrian', 'CR20250604001', 1, 14, 1, NULL, 1, '2025-06-04 00:24:53', NULL, '2025-06-04 19:29:30', 1),
('25060008', '2025-06-04', 'asd', 'asd', 17500.00, 'Menunggu Antrian', 'CR20250604001', 1, 15, 1, NULL, 1, '2025-06-04 00:34:14', NULL, '2025-06-04 19:29:33', 1),
('25060009', '2025-06-04', 'asd', 'asd', 17500.00, 'Selesai', 'CR20250604001', 1, 19, 1, 1, NULL, '2025-06-04 00:34:47', '2025-06-04 00:35:12', NULL, 0),
('25060010', '2025-06-04', 'asd', 'asd', 17500.00, 'Selesai', 'CR20250604001', 1, 23, 1, 1, NULL, '2025-06-04 00:35:35', '2025-06-04 00:35:53', NULL, 0),
('25060011', '2025-06-04', 'asd', 'asd', 17500.00, 'Menunggu Antrian', 'CR20250604001', 1, 24, 1, NULL, 1, '2025-06-04 00:36:16', NULL, '2025-06-04 19:29:35', 1),
('25060012', '2025-06-04', 'asd', 'asd', 17500.00, 'Selesai', 'CR20250604001', 1, 28, 1, 1, NULL, '2025-06-04 00:40:08', '2025-06-04 00:40:31', NULL, 0),
('25060013', '2025-06-04', 'asd', 'asd', 17500.00, 'Selesai', 'CR20250604001', 1, 32, 1, 1, NULL, '2025-06-04 00:41:50', '2025-06-04 00:42:01', NULL, 0),
('25060014', '2025-06-04', 'asd', 'asd', 17500.00, 'Selesai', 'CR20250604001', 1, 36, 1, 1, NULL, '2025-06-04 00:58:55', '2025-06-04 00:59:13', NULL, 0),
('25060015', '2025-06-04', 'asd', 'asd', 17500.00, 'Selesai', 'CR20250604001', 1, 40, 1, 1, NULL, '2025-06-04 01:00:07', '2025-06-04 01:00:20', NULL, 0),
('25060016', '2025-06-04', 'asd', 'asd', 17500.00, 'Selesai', 'CR20250604001', 1, 44, 1, 1, NULL, '2025-06-04 01:01:21', '2025-06-04 01:01:31', NULL, 0),
('25060017', '2025-06-04', 'asd', 'asd', 17500.00, 'Dalam Antrian', 'CR20250604001', 1, 106, 1, 1, 1, '2025-06-04 01:01:53', '2025-06-04 01:46:22', '2025-06-04 01:46:33', 1),
('25060018', '2025-06-04', 'asd', 'asd', 17500.00, 'Selesai', 'CR20250604001', 1, 49, 1, 1, NULL, '2025-06-04 01:01:53', '2025-06-04 01:02:04', NULL, 0),
('25060019', '2025-06-04', 'asd', 'asd', 17500.00, 'Selesai', 'CR20250604001', 1, 53, 1, 1, NULL, '2025-06-04 01:02:19', '2025-06-04 01:02:29', NULL, 0),
('25060020', '2025-06-04', 'asd', 'asd', 17500.00, 'Selesai', 'CR20250604001', 1, 57, 1, 1, NULL, '2025-06-04 01:02:46', '2025-06-04 01:02:57', NULL, 0),
('25060021', '2025-06-04', 'asd', 'asd', 17500.00, 'Selesai', 'CR20250604001', 1, 61, 1, 1, NULL, '2025-06-04 01:03:12', '2025-06-04 01:03:24', NULL, 0),
('25060022', '2025-06-04', 'asd', 'asd', 17500.00, 'Selesai', 'CR20250604001', 1, 65, 1, 1, NULL, '2025-06-04 01:03:40', '2025-06-04 01:03:52', NULL, 0),
('25060023', '2025-06-04', 'asd', 'asd', 17500.00, 'Selesai', 'CR20250604001', 1, 69, 1, 1, NULL, '2025-06-04 01:04:11', '2025-06-04 01:04:23', NULL, 0),
('25060024', '2025-06-04', 'asd', 'asd', 17500.00, 'Selesai', 'CR20250604001', 1, 73, 1, 1, NULL, '2025-06-04 01:07:12', '2025-06-04 01:07:22', NULL, 0),
('25060025', '2025-06-04', 'asd', 'asd', 35000.00, 'Selesai', 'CR20250604003', 1, 77, 1, 1, NULL, '2025-06-04 01:17:44', '2025-06-04 01:17:53', NULL, 0),
('25060026', '2025-06-04', 'asd', 'asd', 35000.00, 'Selesai', 'CR20250604001', 1, 81, 1, 1, NULL, '2025-06-04 01:23:12', '2025-06-04 01:23:22', NULL, 0),
('25060027', '2025-06-04', 'asd', 'asd', 35000.00, 'Selesai', 'CR20250604001', 1, 85, 1, 1, NULL, '2025-06-04 01:23:55', '2025-06-04 01:24:04', NULL, 0),
('25060028', '2025-06-04', 'asd', 'asd', 35000.00, 'Selesai', 'CR20250604001', 1, 89, 1, 1, NULL, '2025-06-04 01:25:17', '2025-06-04 01:25:27', NULL, 0),
('25060029', '2025-06-04', 'asd', 'asd', 17500.00, 'Selesai', 'CR20250604001', 1, 93, 1, 1, NULL, '2025-06-04 01:26:12', '2025-06-04 01:26:21', NULL, 0),
('25060030', '2025-06-04', 'asd', 'asd', 35000.00, 'Selesai', 'CR20250604001', 1, 97, 1, 1, NULL, '2025-06-04 01:26:39', '2025-06-04 01:26:49', NULL, 0),
('25060031', '2025-06-04', 'asd', 'asd', 35000.00, 'Selesai', 'CR20250604001', 1, 101, 1, 1, NULL, '2025-06-04 01:38:17', '2025-06-04 01:38:25', NULL, 0),
('25060032', '2025-06-04', 'asd', 'asd', 25000.00, 'Selesai', 'CR20250604001', 2, 105, 1, 1, NULL, '2025-06-04 01:39:16', '2025-06-04 01:39:31', NULL, 0),
('25060033', '2025-06-04', 'asd', 'asd', 50000.00, 'Selesai', 'CR20250604004', 2, 110, 1, 1, NULL, '2025-06-04 01:46:51', '2025-06-04 01:47:05', NULL, 0),
('25060034', '2025-06-04', 'asd', 'asd', 35000.00, 'Selesai', 'CR20250604005', 1, 121, 1, 1, NULL, '2025-06-04 19:01:47', '2025-06-04 19:19:29', NULL, 0),
('25060035', '2025-06-04', 'asd', 'asd', 50000.00, 'Selesai', 'CR20250604006', 2, 120, 1, 1, NULL, '2025-06-04 19:02:21', '2025-06-04 19:19:23', NULL, 0),
('25060036', '2025-06-04', 'asd', 'asd', 35000.00, 'Dalam Proses', 'CR20250604008', 1, 119, 1, 1, NULL, '2025-06-04 19:03:31', '2025-06-04 19:19:19', NULL, 0),
('25060037', '2025-06-04', 'asd', 'asd', 50000.00, 'Dalam Proses', 'CR20250604009', 2, 149, 1, 1, NULL, '2025-06-04 19:19:40', '2025-06-04 19:44:47', NULL, 0),
('25060038', '2025-06-04', 'asd', '3asd', 50000.00, 'Dalam Proses', 'CR20250604010', 2, 148, 1, 1, NULL, '2025-06-04 19:20:09', '2025-06-04 19:44:39', NULL, 0),
('25060039', '2025-06-04', 'asd', 'asd', 35000.00, 'Dalam Proses', 'CR20250604011', 1, 147, 1, 1, NULL, '2025-06-04 19:24:08', '2025-06-04 19:44:33', NULL, 0),
('25060040', '2025-06-04', 'asd', 'asd', 35000.00, 'Dalam Proses', 'CR20250604012', 1, 139, 1, 1, NULL, '2025-06-04 19:24:24', '2025-06-04 19:30:12', NULL, 0),
('25060041', '2025-06-04', 'asdas', 'asdasd', 35000.00, 'Dalam Proses', 'CR20250604013', 1, 138, 1, 1, NULL, '2025-06-04 19:27:24', '2025-06-04 19:30:05', NULL, 0),
('25060042', '2025-06-04', 'asdsa', 'asd', 35000.00, 'Dalam Proses', 'CR20250604014', 1, 137, 1, 1, NULL, '2025-06-04 19:27:42', '2025-06-04 19:29:58', NULL, 0),
('25060043', '2025-06-04', 'asdas', 'asdas', 35000.00, 'Dalam Proses', 'CR20250604015', 1, 136, 1, 1, NULL, '2025-06-04 19:29:42', '2025-06-04 19:29:52', NULL, 0),
('25060044', '2025-06-04', 'asd', 'asdas', 35000.00, 'Dalam Proses', 'CR20250604016', 1, 142, 1, 1, NULL, '2025-06-04 19:38:46', '2025-06-04 19:38:59', NULL, 0),
('25060045', '2025-06-04', 'asd', 'asd', 35000.00, 'Dalam Proses', 'CR20250604017', 1, 145, 1, 1, NULL, '2025-06-04 19:44:00', '2025-06-04 19:44:21', NULL, 0),
('25060046', '2025-06-04', 'asd', 'asd', 35000.00, 'Dalam Proses', 'CR20250604018', 1, 146, 1, 1, NULL, '2025-06-04 19:44:13', '2025-06-04 19:44:26', NULL, 0),
('25060047', '2025-06-04', 'asd', 'asd', 35000.00, 'Dalam Antrian', 'CR20250604019', 1, 150, 1, 1, NULL, '2025-06-04 19:46:12', '2025-06-04 19:46:15', NULL, 0),
('25060048', '2025-06-04', 'asdas', 'asd', 35000.00, 'Menunggu Antrian', 'CR20250604020', 1, 151, 1, NULL, NULL, '2025-06-04 19:48:10', NULL, NULL, 0);

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
(10, 'Didit', 'hello Dit', '$2a$10$dwCgtSci/eF0.Np67l2PbuU3JWnDtLURzKUXDpuxfxeIuLF/qCCj2', 'Kasir', 1, NULL, NULL, '2025-05-27 23:56:27', NULL, NULL, 0, '123321', 0);

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
  MODIFY `id_absensi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=104;

--
-- AUTO_INCREMENT untuk tabel `barang`
--
ALTER TABLE `barang`
  MODIFY `id_barang` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT untuk tabel `biaya_operasional`
--
ALTER TABLE `biaya_operasional`
  MODIFY `id_biaya` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT untuk tabel `detail_opname_stok`
--
ALTER TABLE `detail_opname_stok`
  MODIFY `id_detail_opname` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `fasilitas`
--
ALTER TABLE `fasilitas`
  MODIFY `id_fasilitas` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT untuk tabel `kartu_stok`
--
ALTER TABLE `kartu_stok`
  MODIFY `id_kartu_stok` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=124;

--
-- AUTO_INCREMENT untuk tabel `karyawan`
--
ALTER TABLE `karyawan`
  MODIFY `id_karyawan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT untuk tabel `layanan`
--
ALTER TABLE `layanan`
  MODIFY `id_layanan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT untuk tabel `log_barang_masuk`
--
ALTER TABLE `log_barang_masuk`
  MODIFY `id_log` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT untuk tabel `opname_stok`
--
ALTER TABLE `opname_stok`
  MODIFY `id_opname` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `pengeluaran_barang`
--
ALTER TABLE `pengeluaran_barang`
  MODIFY `id_pengeluaran` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=184;

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
