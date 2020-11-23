# pmd-idea


![Build](https://github.com/ybroeker/pmd-idea/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/15412-pmd-idea.svg)](https://plugins.jetbrains.com/plugin/15412-pmd-idea)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/15412-pmd-idea.svg)](https://plugins.jetbrains.com/plugin/15412-pmd-idea)


[![Total alerts](https://img.shields.io/lgtm/alerts/g/ybroeker/pmd-idea.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/ybroeker/pmd-idea/alerts/)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/ybroeker/pmd-idea.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/ybroeker/pmd-idea/context:java)

## Use

<!-- Plugin description -->
This plugin provides PMD support within IntelliJ and allows you to scan your code.

To enable PMD, configure a pmd ruleset in the plugin settings and select the appropriate pmd version.

PMD is integrated as an inspection, so that the currently opened file is scanned "on the fly".
Quick fixes are available for some common rules.

To scan your code manually, open the toolbar window and scan your entire project or the currently open file.

PMD versions 6.0.1 to 6.29 are supported and bundled.

<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "pmd-idea"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/ybroeker/pmd-idea/releases/latest) and install it manually using
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
