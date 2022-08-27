<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# pmd-idea Changelog

## [Unreleased]
### Added
- Support for IntelliJ IDEA 2022.1
- Support for IntelliJ IDEA 2022.2

### Changed

### Deprecated

### Removed
- Support for IntelliJ IDEA 2020.3

### Fixed

### Security
## [1.8.0]
### Added
- Support for IntelliJ IDEA 2021.3
- Update supported PMD versions to 6.41

### Changed

### Deprecated

### Removed
- Drop support for IntelliJ IDEA < 2020.3

### Fixed

### Security
## [1.7.0]
### Added
- Update supported PMD versions to 6.38.0

### Changed

### Deprecated

### Removed

### Fixed

### Security
## [1.6.0]
### Added
- Update supported PMD versions to 6.35.0
- Add pmd-xml rules

### Changed
- Support IntelliJ IDEA 2021.1

### Deprecated

### Removed

### Fixed

### Security
## [1.5.0]
### Added
- More quickfixes, see [QUICKFIXES](https://github.com/ybroeker/pmd-idea/blob/main/QUICKFIXES.md) for a complete List
- Update supported PMD versions to 6.34.0

### Changed

### Deprecated

### Removed

### Fixed

### Security
## [1.4.0]
### Changed
- Support IntelliJ IDEA 2021.1

## [1.3.3]
### Fixed
- Remove non-existing files from aux-classpath, see [#15](https://github.com/ybroeker/pmd-idea/issues/15)  

## [1.3.2]
### Fixed
- Fix IndexOutOfBoundsException while running inspection

## [1.3.1]
### Fixed
- Fix NullPointerException while running inspection

## [1.3.0]
### Added
- Add support for PMD 6.30
- More quickfixes, see [QUICKFIXES](https://github.com/ybroeker/pmd-idea/blob/main/QUICKFIXES.md) for a complete List

### Changed
- Show description for rule violation instead generic rule description

## [1.2.0]
### Added
- More quickfixes, see [QUICKFIXES](https://github.com/ybroeker/pmd-idea/blob/main/QUICKFIXES.md) for a complete List
- Quick Fix to suppress warnings for members and class
- Add option to disable on-the-fly inspections

### Changed
- Run inspection only on java files

### Fixed
- Fix possible race-condition while loading pmd rules
- Fix possible exception while running inspection 
- Set auxiliary classpath for pmd

## [1.1.0]
### Added
- Inspections to show violations directly in editor and while inspect code
- Quick Fixes for some inspections, see [QUICKFIXES](https://github.com/ybroeker/pmd-idea/blob/main/QUICKFIXES.md) for a complete List

### Changed
- Hide PMD log messages
- Display the violated rule before the message in the toolwindow, so that it is always visible without scrolling

### Fixed
- Fix incompatibility with older PMD versions

## [1.0.0]
### Added
- Basic functionality
- Initial scaffold created from [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template)
