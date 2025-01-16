# SurvivalRegionClaimPlugin
ダイヤモンドを使用し、土地を保護します<br>
1.8.8のSpigotで動作します<br>

## 依存するプラグイン
* [worldedit-bukkit-6.1.3](https://dev.bukkit.org/projects/worldedit/files/922048)<br>
* [worldguard-6.1](https://dev.bukkit.org/projects/worldguard/files/881691)<br>

## 使い方
1. `/playerregion togglewand` を実行
2. 金の斧を使ってWorldEditと同じように右クリック左クリックで2地点を選択
3. `/playerregion checkprice` を実行し、値段を確認
4. `/playerregion claim <id>` を実行することで、ダイヤを消費し土地を保護できます、保護した土地の名前はidに入力した文字列になります

## コマンド一覧
* `/playerregion togglewand` wandmodeを有効にするか無効にするか変更できます
* `/playerregion claim <id>` ダイヤを消費し土地を保護できます、保護した土地の名前はidに入力した文字列になります
* `/playerregion remove <id>` 土地保護を解除します、その際設定された分のダイヤモンドが返ってきます(Configファイルで量を調整できます)
* `/playerregion list` 保護している土地を確認します
* `/playerregion checkprice` 選択している大きさで土地を保護する場合の値段か確認します(Configファイルで値段を調整できます)
* `/playerregion memberlist <id>` 保護している土地のメンバーを確認します
* `/playerregion addmember <id> <player>` 保護している土地にメンバーを追加します
* `/playerregion removemember <id> <player>` 保護している土地のメンバーを消去します

## Config
* `blockprice` 1ブロック当たりのダイヤモンドの金額を指定できます、小数点以下は切り上げられます(1ブロック`0.5`ダイヤモンドで設定した場合、2ブロックまで1ダイヤモンドで保護できる)
* `regionlimit` 保護できる土地の数の上限を指定できます
* `removereturnpricemultiplier` 土地を消去した際に帰ってくるダイヤの割合を指定できます(`0.0`で1つも返ってこない、`1.0`で全額返ってくる)
* `worldblacklist` ネザーなど、土地保護をさせたくないワールドを指定できます
* `wandtype` 土地保護の範囲指定に使用するアイテムを指定できます

## Forkする場合
このディレクトリにlibフォルダを作成して、その中に依存するプラグインと同じバージョンのjarファイルを入れてください<br>
