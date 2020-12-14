<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# pmd-idea Changelog

## [Unreleased]
### Added
- Add support for PMD 6.30

### Changed
- Show description for rule violation instead generic rule description

### Deprecated

### Removed

### Fixed

### Security
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
