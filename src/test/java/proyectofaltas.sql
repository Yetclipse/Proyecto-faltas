-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 20-10-2025 a las 18:38:30
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `proyectofaltas`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `docentes`
--

CREATE TABLE `docentes` (
  `Cedula` varchar(9) NOT NULL,
  `Nombre` varchar(15) NOT NULL,
  `Apellido` varchar(15) NOT NULL,
  `Materia` varchar(30) NOT NULL,
  `Turno` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `docentes`
--

INSERT INTO `docentes` (`Cedula`, `Nombre`, `Apellido`, `Materia`, `Turno`) VALUES
('123456789', 'pepe', 'epep', 'hist', 'mat'),
('48773615', 'marcela', 'mederos', 'programacion', 'matutino'),
('5678804', 'Rodri', 'Planta', 'Apt', 'Nunca'),
('6666', 'ale', 'dom', 'prog bd', 'mat ves'),
('98765432', 'Enzo', 'Falcón', 'Matematica', 'Vespertino');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `licencias`
--

CREATE TABLE `licencias` (
  `id` int(11) NOT NULL,
  `docente_ci` varchar(9) NOT NULL,
  `Fecha_inicio` date NOT NULL,
  `Fecha_fin` date NOT NULL,
  `Motivo` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `licencias`
--

INSERT INTO `licencias` (`id`, `docente_ci`, `Fecha_inicio`, `Fecha_fin`, `Motivo`) VALUES
(9, '48773615', '2025-09-25', '2025-09-26', 'falta'),
(13, '48773615', '2025-09-30', '2025-10-04', 'Medico'),
(14, '6666', '2025-10-28', '2025-11-12', 'Vacaciones'),
(27, '5678804', '2025-10-15', '2025-10-24', 'yo'),
(30, '5678804', '2025-10-03', '2025-10-10', 'pq si'),
(31, '6666', '2025-10-23', '2025-11-12', 'personal'),
(35, '98765432', '2025-10-21', '2025-10-23', 'Mario'),
(36, '48773615', '2025-10-08', '2025-10-21', 'paseo');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `licencias_grupos`
--

CREATE TABLE `licencias_grupos` (
  `licencia_id` int(11) NOT NULL,
  `grupo_codigo` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `licencias_grupos`
--

INSERT INTO `licencias_grupos` (`licencia_id`, `grupo_codigo`) VALUES
(27, '1MB'),
(27, '1ME'),
(27, '1MG'),
(30, '1MA'),
(30, '1MC'),
(30, '1ME'),
(30, '1MG'),
(31, '2MA'),
(31, '2MB'),
(35, '1MA'),
(35, '1MB'),
(35, '1MC'),
(36, '1MA');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `docentes`
--
ALTER TABLE `docentes`
  ADD PRIMARY KEY (`Cedula`);

--
-- Indices de la tabla `licencias`
--
ALTER TABLE `licencias`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_lic_docente_ci` (`docente_ci`);

--
-- Indices de la tabla `licencias_grupos`
--
ALTER TABLE `licencias_grupos`
  ADD PRIMARY KEY (`licencia_id`,`grupo_codigo`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `licencias`
--
ALTER TABLE `licencias`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `licencias`
--
ALTER TABLE `licencias`
  ADD CONSTRAINT `fk_lic_docente_ci` FOREIGN KEY (`docente_ci`) REFERENCES `docentes` (`Cedula`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_licencias_docentes` FOREIGN KEY (`docente_ci`) REFERENCES `docentes` (`Cedula`) ON UPDATE CASCADE;

--
-- Filtros para la tabla `licencias_grupos`
--
ALTER TABLE `licencias_grupos`
  ADD CONSTRAINT `fk_lg_lic` FOREIGN KEY (`licencia_id`) REFERENCES `licencias` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
