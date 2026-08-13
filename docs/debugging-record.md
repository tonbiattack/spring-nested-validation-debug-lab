# ネストDTO検証漏れのデバッグ記録

## 期待する契約

注文作成APIは、配送先の郵便番号が空であれば注文を作成せず、HTTP 400を返します。検証エラー本文には `shippingAddress.postalCode` を含めます。

| 項目 | 期待値 |
| --- | --- |
| HTTPステータス | 400 |
| エラーコード | `VALIDATION_ERROR` |
| エラーフィールド | `shippingAddress.postalCode` |
| 注文サービスの記録件数 | 0件 |

## バグ状態での観測

初期コミットで `mvn test` を実行しました。実行ログは [01-bug-reproduction.log](01-bug-reproduction.log) です。

| 観測項目 | 実測値 | 判断 |
| --- | --- | --- |
| HTTPステータス | 201 | 無効なリクエストがコントローラーを通過している |
| レスポンス本文 | 作成済み注文 | 検証エラーではなく注文作成の結果が返っている |
| 注文サービスの記録件数 | 1件 | 最終副作用まで実行されている |
| `customerId` を空にした場合 | 400 | 親DTOの検証と例外ハンドラーは動作している |
| `shippingAddress` をnullにした場合 | 400 | `@NotNull` は配送先の存在を検証している |

親DTOの検証は有効であり、例外ハンドラーも機能していました。配送先オブジェクトが存在し、その内側の郵便番号だけが空の場合に限って検証が行われませんでした。

## 原因

`CreateOrderRequest` は配送先へ `@NotNull` を付けていましたが、配送先DTOをカスケード検証する `@Valid` がありませんでした。

```java
@NotNull ShippingAddress shippingAddress
```

`@NotNull` は参照が存在することを検証します。参照先である `ShippingAddress` の `@NotBlank` まで検証するには、親DTO側の参照へ `@Valid` が必要です。

## 修正

配送先フィールドへ `@Valid` を追加しました。

```java
@NotNull @Valid ShippingAddress shippingAddress
```

修正後のログは [02-fixed-verification.log](02-fixed-verification.log) です。空の郵便番号は `shippingAddress.postalCode` の検証エラーとしてHTTP 400になり、注文サービスの記録件数は0件です。正しい郵便番号ではHTTP 201となり、注文が1件作成されます。

## 回帰検証

統合テストは、HTTPステータス、エラー本文のフィールド名、注文数を同時に検証します。これにより、例外が出たことだけでなく、無効な入力が注文作成の副作用まで到達しないことを確認します。
