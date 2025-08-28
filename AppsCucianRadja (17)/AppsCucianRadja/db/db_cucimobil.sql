CREATE DATABASE IF NOT EXISTS `db_cucimobil`;

USE `db_cucimobil`;

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id_users` INT NOT NULL AUTO_INCREMENT,
  `nama` VARCHAR(45) NOT NULL,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `role` VARCHAR(45) NOT NULL,
  `insert_by` INT DEFAULT NULL,
  `update_by` INT DEFAULT NULL,
  `delete_by` INT DEFAULT NULL,
  `insert_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_at` DATETIME DEFAULT NULL,
  `delete_at` DATETIME DEFAULT NULL,
  `is_delete` TINYINT NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_users`),
  KEY `users_1_idx` (`insert_by`),
  KEY `users_2_idx` (`update_by`),
  KEY `users_3_idx` (`delete_by`),
  CONSTRAINT `users_1` FOREIGN KEY (`insert_by`) REFERENCES `users` (`id_users`),
  CONSTRAINT `users_2` FOREIGN KEY (`update_by`) REFERENCES `users` (`id_users`),
  CONSTRAINT `users_3` FOREIGN KEY (`delete_by`) REFERENCES `users` (`id_users`)
) ENGINE=INNODB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

/*Data for the table `users` */

INSERT  INTO `users`(`id_users`,`nama`,`username`,`password`,`role`,`insert_by`,`update_by`,`delete_by`,`insert_at`,`update_at`,`delete_at`,`is_delete`) VALUES 
(1,'Administrator','admin','$2a$10$QmR3/VGbWgQ0IwFfHWFEmuKEN1uiuCSqW63QBoL1whz3hg6VhCnv6','Owner',NULL,NULL,NULL,'2025-03-04 18:50:31',NULL,NULL,0),
(2,'Eric','eric','$2a$10$SXgIoS9At7DRtFzgEg4la..8LeUIWOxApw516DBnBnLnbjAWkn/8K','Kasir',1,NULL,NULL,'2025-03-06 14:05:15',NULL,NULL,0);

/*Table structure for table `pelanggan` */

DROP TABLE IF EXISTS `pelanggan`;

CREATE TABLE `pelanggan` (
  `id_pelanggan` CHAR(13) NOT NULL,
  `nama_pelanggan` VARCHAR(45) NOT NULL,
  `telepon` VARCHAR(45) NOT NULL,
  `alamat` VARCHAR(255) NOT NULL,
  `insert_by` INT DEFAULT NULL,
  `update_by` INT DEFAULT NULL,
  `delete_by` INT DEFAULT NULL,
  `insert_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_at` DATETIME DEFAULT NULL,
  `delete_at` DATETIME DEFAULT NULL,
  `is_delete` TINYINT NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_pelanggan`),
  KEY `users_1_idx` (`insert_by`),
  KEY `users_2_idx` (`update_by`),
  KEY `users_3_idx` (`delete_by`),
  CONSTRAINT `pelanggan_1` FOREIGN KEY (`insert_by`) REFERENCES `users` (`id_users`),
  CONSTRAINT `pelanggan_2` FOREIGN KEY (`update_by`) REFERENCES `users` (`id_users`),
  CONSTRAINT `pelanggan_3` FOREIGN KEY (`delete_by`) REFERENCES `users` (`id_users`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb3;

/*Data for the table `pelanggan` */

INSERT  INTO `pelanggan`(`id_pelanggan`,`nama_pelanggan`,`telepon`,`alamat`,`insert_by`,`update_by`,`delete_by`,`insert_at`,`update_at`,`delete_at`,`is_delete`) VALUES 
('CR20250107001','Andi Saputra','081212345678','Jl. Raya Bogor No.15, Depok',1,NULL,NULL,'2025-03-07 17:39:00',NULL,NULL,0),
('CR20250107002','Fitri Ayu','082312345678','Jl. Ahmad Yani No.45, Bekasi',1,NULL,NULL,'2025-03-07 17:39:00',NULL,NULL,0),
('CR20250107003','Rizki Hidayat','083412345678','Jl. KH Hasyim No.25, Jakarta',1,NULL,NULL,'2025-03-07 17:39:00',NULL,NULL,0),
('CR20250107004','Siti Aminah','084512345678','Jl. Gatot Subroto No.30, Tangerang',1,NULL,NULL,'2025-03-07 17:39:00',NULL,NULL,0),
('CR20250207005','Andik Firmansyah','081212345678','Jl. Raya Bogor No.15, Depok',1,1,NULL,'2025-03-07 17:39:00','2025-03-09 04:42:25',NULL,0),
('CR20250207006','Fitri Arum','082312345678','Jl. Ahmad Yani No.45, Bekasi',1,NULL,NULL,'2025-03-07 17:39:00',NULL,NULL,0),
('CR20250207007','Rizki Harun','083412345678','Jl. KH Hasyim No.25, Jakarta',1,NULL,NULL,'2025-03-07 17:39:00',NULL,NULL,0),
('CR20250207008','Siti Aisyah','084512345678','Jl. Gatot Subroto No.30, Tangerang',1,NULL,NULL,'2025-03-07 17:39:00',NULL,NULL,0),
('CR20250307009','Uzumaki Naruto','081212345678','Jl. Raya Bogor No.15, Depok',1,NULL,NULL,'2025-03-07 17:39:00',NULL,NULL,0),
('CR20250307010','Uchiha Sasuke','082312345678','Jl. Ahmad Yani No.45, Bekasi',1,NULL,NULL,'2025-03-07 17:39:00',NULL,NULL,0),
('CR20250307011','Sakura Haruno','083412345678','Jl. KH Hasyim No.25, Jakarta',1,NULL,NULL,'2025-03-07 17:39:00',NULL,NULL,0),
('CR20250307012','Hatake Kakashi','084512345678','Jl. Gatot Subroto No.30, Tangerang',1,NULL,NULL,'2025-03-07 17:39:00',NULL,NULL,0),
('CR20250307013','Tony','089','Bekasi',1,NULL,NULL,'2025-03-07 19:08:08',NULL,NULL,0),
('CR20250307014','Stack','078','Banten',1,NULL,NULL,'2025-03-07 19:34:18',NULL,NULL,0),
('CR20250307015','Roger','056','Jakarta',1,1,NULL,'2025-03-07 19:36:43','2025-03-09 04:41:48',NULL,0);

/*Table structure for table `karyawan` */

DROP TABLE IF EXISTS `karyawan`;

CREATE TABLE `karyawan` (
  `id_karyawan` INT NOT NULL AUTO_INCREMENT,
  `nama_karyawan` VARCHAR(45) NOT NULL,
  `telepon` VARCHAR(45) NOT NULL,
  `alamat` VARCHAR(255) NOT NULL,
  `insert_by` INT DEFAULT NULL,
  `update_by` INT DEFAULT NULL,
  `delete_by` INT DEFAULT NULL,
  `insert_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_at` DATETIME DEFAULT NULL,
  `delete_at` DATETIME DEFAULT NULL,
  `is_delete` TINYINT NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_karyawan`),
  KEY `karyawan_1_idx` (`insert_by`),
  KEY `karyawan_2_idx` (`update_by`),
  KEY `karyawan_3_idx` (`delete_by`),
  CONSTRAINT `karyawan_1` FOREIGN KEY (`insert_by`) REFERENCES `users` (`id_users`),
  CONSTRAINT `karyawan_2` FOREIGN KEY (`update_by`) REFERENCES `users` (`id_users`),
  CONSTRAINT `karyawan_3` FOREIGN KEY (`delete_by`) REFERENCES `users` (`id_users`)
) ENGINE=INNODB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;

/*Data for the table `karyawan` */

INSERT  INTO `karyawan`(`id_karyawan`,`nama_karyawan`,`telepon`,`alamat`,`insert_by`,`update_by`,`delete_by`,`insert_at`,`update_at`,`delete_at`,`is_delete`) VALUES 
(1,'Budi Santoso','081234567890','Jl. Merdeka No.1, Jakarta',1,NULL,NULL,'2025-03-07 17:28:14',NULL,NULL,0),
(2,'Doni Setiawan','082345678901','Jl. Sudirman No.10, Bandung',1,NULL,NULL,'2025-03-07 17:28:14',NULL,NULL,0),
(3,'Siti Rohmah','083456789012','Jl. Diponegoro No.20, Bekasi',1,NULL,NULL,'2025-03-07 17:28:14',NULL,NULL,0),
(4,'Rudi Hartono','084567890123','Jl. Thamrin No.5, Tangerang',1,1,NULL,'2025-03-07 17:28:14','2025-03-09 04:41:21',NULL,0);

/*Table structure for table `barang` */

DROP TABLE IF EXISTS `barang`;

CREATE TABLE `barang` (
  `id_barang` INT NOT NULL AUTO_INCREMENT,
  `nama_barang` VARCHAR(45) NOT NULL,
  `satuan` VARCHAR(45) NOT NULL,
  `stok` INT NOT NULL,
  `insert_by` INT DEFAULT NULL,
  `update_by` INT DEFAULT NULL,
  `delete_by` INT DEFAULT NULL,
  `insert_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_at` DATETIME DEFAULT NULL,
  `delete_at` DATETIME DEFAULT NULL,
  `is_delete` TINYINT NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_barang`),
  KEY `barang_1_idx` (`insert_by`),
  KEY `barang_2_idx` (`update_by`),
  KEY `barang_3_idx` (`delete_by`),
  CONSTRAINT `karyawan_10` FOREIGN KEY (`insert_by`) REFERENCES `users` (`id_users`),
  CONSTRAINT `karyawan_20` FOREIGN KEY (`update_by`) REFERENCES `users` (`id_users`),
  CONSTRAINT `karyawan_30` FOREIGN KEY (`delete_by`) REFERENCES `users` (`id_users`)
) ENGINE=INNODB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;

/*Data for the table `barang` */

INSERT  INTO `barang`(`id_barang`,`nama_barang`,`satuan`,`stok`,`insert_by`,`update_by`,`delete_by`,`insert_at`,`update_at`,`delete_at`,`is_delete`) VALUES 
(1,'Sabun Cuci Mobil','ml',4100,1,NULL,NULL,'2025-03-07 17:27:36',NULL,NULL,0),
(2,'Shampo Mobil','liter',5,1,NULL,NULL,'2025-03-07 17:27:36',NULL,NULL,0),
(3,'Lap Microfiber','pcs',88,1,NULL,NULL,'2025-03-07 17:27:36',NULL,NULL,0),
(4,'Wax Premium','botol',14,1,NULL,NULL,'2025-03-07 17:27:36',NULL,NULL,0),
(5,'Semir Ban','ml',2000,1,NULL,NULL,'2025-03-07 17:27:36',NULL,NULL,0);

/*Table structure for table `fasilitas` */

DROP TABLE IF EXISTS `fasilitas`;

CREATE TABLE `fasilitas` (
  `id_fasilitas` INT NOT NULL AUTO_INCREMENT,
  `nama_fasilitas` VARCHAR(45) NOT NULL,
  `insert_by` INT DEFAULT NULL,
  `update_by` INT DEFAULT NULL,
  `delete_by` INT DEFAULT NULL,
  `insert_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_at` DATETIME DEFAULT NULL,
  `delete_at` DATETIME DEFAULT NULL,
  `is_delete` TINYINT NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_fasilitas`),
  KEY `layanan_1_idx` (`insert_by`),
  KEY `layanan_2_idx` (`update_by`),
  KEY `layanan_3_idx` (`delete_by`),
  CONSTRAINT `layanan_10` FOREIGN KEY (`insert_by`) REFERENCES `users` (`id_users`),
  CONSTRAINT `layanan_20` FOREIGN KEY (`update_by`) REFERENCES `users` (`id_users`),
  CONSTRAINT `layanan_30` FOREIGN KEY (`delete_by`) REFERENCES `users` (`id_users`)
) ENGINE=INNODB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;

/*Data for the table `fasilitas` */

INSERT  INTO `fasilitas`(`id_fasilitas`,`nama_fasilitas`,`insert_by`,`update_by`,`delete_by`,`insert_at`,`update_at`,`delete_at`,`is_delete`) VALUES 
(1,'Cuci Salju',1,NULL,NULL,'2025-03-07 17:27:56',NULL,NULL,0),
(2,'Cuci Interior',1,NULL,NULL,'2025-03-07 17:27:56',NULL,NULL,0),
(3,'Poles Body',1,NULL,NULL,'2025-03-07 17:27:56',NULL,NULL,0),
(4,'Poles Kaca',1,NULL,NULL,'2025-03-07 17:27:56',NULL,NULL,0),
(5,'Pembersihan Mesin',1,NULL,NULL,'2025-03-07 17:27:56',NULL,NULL,0);

/*Table structure for table `fasilitas_detail` */

DROP TABLE IF EXISTS `fasilitas_detail`;

CREATE TABLE `fasilitas_detail` (
  `id_fasilitas` INT NOT NULL,
  `id_barang` INT NOT NULL,
  `jumlah` INT NOT NULL,
  KEY `fasilitas_detail_1_idx` (`id_fasilitas`),
  KEY `fasilitas_detail_2_idx` (`id_barang`),
  CONSTRAINT `fasilitas_detail_1` FOREIGN KEY (`id_fasilitas`) REFERENCES `fasilitas` (`id_fasilitas`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fasilitas_detail_2` FOREIGN KEY (`id_barang`) REFERENCES `barang` (`id_barang`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb3;

/*Data for the table `fasilitas_detail` */

INSERT  INTO `fasilitas_detail`(`id_fasilitas`,`id_barang`,`jumlah`) VALUES 
(1,1,100),
(1,2,5),
(2,3,2),
(3,4,1),
(4,4,1),
(5,2,2);


/*Table structure for table `layanan` */

DROP TABLE IF EXISTS `layanan`;

CREATE TABLE `layanan` (
  `id_layanan` INT NOT NULL AUTO_INCREMENT,
  `nama_layanan` VARCHAR(45) NOT NULL,
  `harga` DECIMAL(15,2) NOT NULL,
  `insert_by` INT DEFAULT NULL,
  `update_by` INT DEFAULT NULL,
  `delete_by` INT DEFAULT NULL,
  `insert_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_at` DATETIME DEFAULT NULL,
  `delete_at` DATETIME DEFAULT NULL,
  `is_delete` TINYINT NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_layanan`),
  KEY `layanan_1_idx` (`insert_by`),
  KEY `layanan_2_idx` (`update_by`),
  KEY `layanan_3_idx` (`delete_by`),
  CONSTRAINT `layanan_1` FOREIGN KEY (`insert_by`) REFERENCES `users` (`id_users`),
  CONSTRAINT `layanan_2` FOREIGN KEY (`update_by`) REFERENCES `users` (`id_users`),
  CONSTRAINT `layanan_3` FOREIGN KEY (`delete_by`) REFERENCES `users` (`id_users`)
) ENGINE=INNODB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;

/*Data for the table `layanan` */

INSERT  INTO `layanan`(`id_layanan`,`nama_layanan`,`harga`,`insert_by`,`update_by`,`delete_by`,`insert_at`,`update_at`,`delete_at`,`is_delete`) VALUES 
(1,'Cuci Mobil Reguler',35000.00,1,NULL,NULL,'2025-03-07 17:28:27',NULL,NULL,0),
(2,'Cuci Mobil Premium',50000.00,1,NULL,NULL,'2025-03-07 17:28:27',NULL,NULL,0),
(3,'Poles Body',75000.00,1,NULL,NULL,'2025-03-07 17:28:27',NULL,NULL,0),
(4,'Cuci Motor',20000.00,1,NULL,NULL,'2025-03-07 17:28:27',NULL,NULL,0);

/*Table structure for table `layanan_detail` */

DROP TABLE IF EXISTS `layanan_detail`;

CREATE TABLE `layanan_detail` (
  `id_layanan` INT NOT NULL,
  `id_fasilitas` INT NOT NULL,
  KEY `layanan_detail_1_idx` (`id_layanan`),
  KEY `layanan_detail_2_idx` (`id_fasilitas`),
  CONSTRAINT `layanan_detail_1` FOREIGN KEY (`id_layanan`) REFERENCES `layanan` (`id_layanan`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `layanan_detail_2` FOREIGN KEY (`id_fasilitas`) REFERENCES `fasilitas` (`id_fasilitas`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb3;

/*Data for the table `layanan_detail` */

INSERT  INTO `layanan_detail`(`id_layanan`,`id_fasilitas`) VALUES 
(1,1),
(2,1),
(2,2),
(3,3),
(3,4),
(4,1);

/*Table structure for table `pesanan` */

DROP TABLE IF EXISTS `pesanan`;

CREATE TABLE `pesanan` (
  `id_pesanan` CHAR(8) NOT NULL,
  `tanggal_pesanan` DATE NOT NULL,
  `merk_mobil` VARCHAR(45) NOT NULL,
  `nomor_plat` VARCHAR(45) NOT NULL,
  `total_harga` DECIMAL(15,2) DEFAULT NULL,
  `status` VARCHAR(45) NOT NULL,
  `id_pelanggan` CHAR(13) NOT NULL,
  `id_layanan` INT NOT NULL,
  `no_antrian` INT DEFAULT NULL,
  `insert_by` INT DEFAULT NULL,
  `update_by` INT DEFAULT NULL,
  `delete_by` INT DEFAULT NULL,
  `insert_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_at` DATETIME DEFAULT NULL,
  `delete_at` DATETIME DEFAULT NULL,
  `is_delete` TINYINT NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_pesanan`),
  KEY `pesanan_1_idx` (`insert_by`),
  KEY `pesanan_2_idx` (`update_by`),
  KEY `pesanan_3_idx` (`delete_by`),
  KEY `pesanan_4_idx` (`id_pelanggan`),
  KEY `pesanan_5_idx` (`id_layanan`),
  CONSTRAINT `pesanan_1` FOREIGN KEY (`insert_by`) REFERENCES `users` (`id_users`),
  CONSTRAINT `pesanan_2` FOREIGN KEY (`update_by`) REFERENCES `users` (`id_users`),
  CONSTRAINT `pesanan_3` FOREIGN KEY (`delete_by`) REFERENCES `users` (`id_users`),
  CONSTRAINT `pesanan_4` FOREIGN KEY (`id_pelanggan`) REFERENCES `pelanggan` (`id_pelanggan`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `pesanan_5` FOREIGN KEY (`id_layanan`) REFERENCES `layanan` (`id_layanan`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb3;

/*Data for the table `pesanan` */

INSERT  INTO `pesanan`(`id_pesanan`,`tanggal_pesanan`,`merk_mobil`,`nomor_plat`,`total_harga`,`status`,`id_pelanggan`,`id_layanan`,`no_antrian`,`insert_by`,`update_by`,`delete_by`,`insert_at`,`update_at`,`delete_at`,`is_delete`) VALUES 
('25010001','2025-01-05','Toyota Avanza','B 1111 AAA',35000.00,'Selesai','CR20250107001',1,1,1,NULL,NULL,'2025-03-07 17:41:19',NULL,NULL,0),
('25010002','2025-01-10','Honda Brio','B 2222 BBB',50000.00,'Selesai','CR20250107002',2,2,1,1,NULL,'2025-03-07 17:41:19','2025-03-07 17:42:56',NULL,0),
('25010003','2025-01-15','Mitsubishi Pajero','B 3333 CCC',75000.00,'Selesai','CR20250107003',3,3,1,1,NULL,'2025-03-07 17:41:19','2025-03-07 17:42:57',NULL,0),
('25010004','2025-01-20','Yamaha NMax','B 4444 DDD',20000.00,'Selesai','CR20250107004',4,5,1,1,NULL,'2025-03-07 17:41:19','2025-03-07 17:43:00',NULL,0),
('25020005','2025-02-05','Daihatsu Xenia','B 5555 EEE',35000.00,'Selesai','CR20250207005',1,1,1,NULL,NULL,'2025-03-07 17:41:19',NULL,NULL,0),
('25020006','2025-02-10','Suzuki Ertiga','B 6666 FFF',50000.00,'Selesai','CR20250207006',2,2,1,1,NULL,'2025-03-07 17:41:19','2025-03-07 17:43:56',NULL,0),
('25020007','2025-02-15','Nissan Livina','B 7777 GGG',75000.00,'Selesai','CR20250207007',3,3,1,1,NULL,'2025-03-07 17:41:19','2025-03-07 17:43:58',NULL,0),
('25020008','2025-02-20','Honda Beat','B 8888 HHH',20000.00,'Selesai','CR20250207008',4,6,1,1,NULL,'2025-03-07 17:41:19','2025-03-07 17:43:59',NULL,0),
('25030009','2025-03-07','Toyota Avanza','B 1234 ABC',35000.00,'Selesai','CR20250307009',1,1,1,NULL,NULL,'2025-03-07 17:41:19',NULL,NULL,0),
('25030010','2025-03-07','Honda Brio','B 5678 DEF',50000.00,'Selesai','CR20250307010',2,2,1,1,NULL,'2025-03-07 17:41:19','2025-03-07 17:44:01',NULL,0),
('25030011','2025-03-07','Mitsubishi Pajero','B 9012 GHI',75000.00,'Selesai','CR20250307011',3,3,1,1,NULL,'2025-03-07 17:41:19','2025-03-07 17:44:02',NULL,0),
('25030012','2025-03-07','Yamaha NMax','B 3456 JKL',20000.00,'Selesai','CR20250307012',4,7,1,1,NULL,'2025-03-07 17:41:19','2025-03-07 17:44:04',NULL,0),
('25030013','2025-03-07','Honda','B 999 JKT',50000.00,'Selesai','CR20250307013',2,8,1,1,NULL,'2025-03-07 19:08:08','2025-03-07 19:08:30',NULL,0),
('25030014','2025-03-07','Suzuki','B 888 BTN',50000.00,'Selesai','CR20250307014',2,9,1,1,NULL,'2025-03-07 19:34:18','2025-03-07 19:34:29',NULL,0),
('25030015','2025-03-07','Honda','B 777 GHJ',50000.00,'Selesai','CR20250307015',2,10,1,1,NULL,'2025-03-07 19:36:43','2025-03-07 19:37:05',NULL,0);


