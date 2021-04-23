# QUICKFIXES

This plugin contains QuickFixes for the following rules:

- MethodArgumentCouldBeFinal
- LocalVariableCouldBeFinal
- CommentDefaultAccessModifier
  - `/* default */` or `/* package */`  can be added as a comment
- UselessParentheses
- UseVarargs 
- UnnecessaryConstructor
- CallSuperInConstructor
- FieldNamingConventions
  - opens IntelliJs rename dialog
- MissingSerialVersionUID
- UseLocaleWithCaseConversions
  - Inserts `Locale.ROOT` or `Locale.getDefault()`
- ClassWithOnlyPrivateConstructorsShouldBeFinal

Additionally, `@SuppressWarnings` can be automatically applied to members or classes for a specific rule.
