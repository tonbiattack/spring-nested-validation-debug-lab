# 参照資料メモ

## Hibernate Validator Reference Guide

- URL: https://docs.hibernate.org/stable/validator/reference/en-US/html_single/
- 節: 2.1.6 Object graphs
- 確認内容: 別オブジェクトへの参照を持つフィールドまたはプロパティに `@Valid` を付けると、参照先のオブジェクトも検証されます。
- 確認内容: カスケード検証は再帰的に行われ、ネストした参照にも `@Valid` があれば検証対象になります。
- 記事での利用: `shippingAddress` 自体のnull検証だけでは内側の`postalCode`を検証しない理由と、`@Valid`を親DTOの参照へ付ける修正の根拠として用います。

## Jakarta EE Tutorial

- URL: https://jakarta.ee/learn/docs/jakartaee-tutorial/current/beanvalidation/bean-validation-advanced/bean-validation-advanced.html
- 節: Cascading validation
- 確認内容: 検証するオブジェクトのメンバーへ `@Valid` を指定すると、そのメンバーをカスケード検証できます。
- 記事での利用: 親DTOから配送先DTOを検証対象に含める修正の仕様根拠として用います。

## Spring Framework Reference Documentation

- URL: https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods/validation.html
- 確認結果: 取得時点で404だったため、記事の根拠としては利用しません。
