# Spring Boot ネストDTO検証デバッグラボ

ネストした配送先DTOに `@Valid` を付け忘れたことで、空の郵便番号を含む注文リクエストが `201 Created` で受理される不具合を再現し、修正するSpring Bootプロジェクトです。

## 対象となる契約

`POST /orders` は、顧客IDと配送先を受け取ります。配送先の郵便番号が空なら、注文を作成せず `400 Bad Request` を返すことが契約です。

| 入力 | 期待するHTTP応答 | 期待する注文数 |
| --- | --- | --- |
| 郵便番号が空 | `400 Bad Request` | 0件 |
| 郵便番号が有効 | `201 Created` | 1件 |

## 必要な環境

Java 21とMavenが必要です。このプロジェクトはSpring Boot 3.3.12を使用しています。

## 最新状態の確認

最新のmainブランチでは、ネストした配送先DTOへカスケード検証を適用しています。次のコマンドで統合テストを実行してください。

```bash
mvn test
```

空の郵便番号では `400` と `shippingAddress.postalCode` のエラーが返り、注文は作成されません。正しい入力では `201` と注文作成を確認します。

## バグ状態の再現

不具合状態はGit履歴に残しています。初期コミットをチェックアウトすると、空の郵便番号が `201` で受理され、テストが失敗します。

```bash
git checkout f92de4e
mvn test
```

確認後はmainブランチへ戻します。

```bash
git switch main
mvn test
```

## 修正内容

修正前は、配送先オブジェクトの存在だけを `@NotNull` で確認していました。

```java
@NotNull ShippingAddress shippingAddress
```

修正後は、配送先の内部プロパティまで検証するために `@Valid` を追加します。

```java
@NotNull @Valid ShippingAddress shippingAddress
```

## 関連資料

調査過程は [docs/debugging-record.md](docs/debugging-record.md)、テスト実行ログは `docs/01-bug-reproduction.log` と `docs/02-fixed-verification.log`、公式資料の確認メモは [docs/references.md](docs/references.md) にあります。
