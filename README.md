# ItemRegister 플러그인 사용법
플러그인 사용법을 잊어버린 당신을 위한 플러그인 사용 설명서입니다.
이 플러그인에 목적성이라곤 없고, 여러 편의성 기능들을 만들어 두었습니다.
커맨드들과, 커맨드들을 조합하여 사용 가능한 기능에 대해서 적어 두겠습니다.

# 커맨드 목록

| 커맨드                                       | 설명                                    | 줄임말 |
|-------------------------------------------|---------------------------------------|-----|
| [itemevent](#itemevent)                   | 아이템에 이벤트를 등록합니다.                      | ie  |
| [persistentdata](#persistentdata)         | 아이템의 저장소에 데이터를 집어넣습니다.                | pd  |
| [customrecipe](#customrecipe)             | 레시피를 제작합니다.                           | cr  |
| [recipeannouncement](#recipeannouncement) | 특정 레시피 제작 시 서버 전체에 알림이 가며, 칭호를 지급합니다. | ra  |
| [custominventory](#custominventory)       | GUI를 제작합니다.                           | ci  |
| [prefix](#prefix)                         | 칭호를 제작합니다.                            |     |
| [debugdata](#debugdata)                   | 들고 있는 아이템의 정보를 출력합니다.                 |     |
| [help](#help)                             | help라는 이름의 GUI를 엽니다.                  |     |
| [fly](#fly)                               | 남은 플라이 시간을 확인합니다.                     |     |
| [커스텀레시피](#커스텀레시피)                         | 커스텀 레시피를 완성 아이템 이름으로 검색합니다.           |     |

# itemevent
Aliases: `/ie`
<br>Usage
```
/itemevent <이벤트>
```
사용한 플레이어가 들고 있는 아이템을 이벤트에 등록합니다.
## 이벤트
| 이벤트         | 설명                                                                                                                     |
|-------------|------------------------------------------------------------------------------------------------------------------------|
| death       | 아이템을 가진 채 사망 시, 인벤토리에서 아이템 스택이 1 감소하며 인벤토리와 경험치가 보존됩니다.                                                                |
| fly5m       | 아이템을 우클릭 시 사용 가능한 플라이 시간이 5분 증가합니다.                                                                                    |
| locate      | 아이템을 우클릭 시 아이템의 [itemRegister:structure](#persistentdata)와 itemRegister:locate 저장소에서 각각 구조물과 바이옴을 얻어 위치를 플레이어에게 출력합니다. |
| prefix      | 아이템을 우클릭 시 아이템의 itemRegister:prefix 저장소에서 prefix을 얻어 플레이어에게 지급합니다.                                                     |
| itemcommand | 아이템을 우클릭 시 만약 플레이어가 아이템의 itemRegister:required 저장소에 있는 칭호를 가지고 있을 경우 아이템의 itemRegister:itemcommand 저장소에 담긴 커맨드를 실행합니다. |

# persistentdata
Aliases: `/pd`
<br>Usage
```
/persistentdata list
/persistentdata remove <namespacedkey>
/persistentdata set structure <structure>
/persistentdata set <type> ...
```
아이템의 PublicBukkitValues NBT를 관리하는 커맨드입니다.
set으로 NBT를 설정하고, remove로 지울 수 있으며, list로 조회할 수 있습니다
## type
| 타입          | ...                                                                                                                                             |
|-------------|-------------------------------------------------------------------------------------------------------------------------------------------------|
| structure   | 구조물 ID ([#](https://minecraft.fandom.com/ko/wiki/%EC%83%9D%EC%84%B1_%EA%B5%AC%EC%A1%B0%EB%AC%BC#ID) 링크의 Java Edition 열 참고)                      |
| biome       | 바이옴 ID ([#](https://minecraft.fandom.com/ko/wiki/%EC%83%9D%EB%AC%BC_%EA%B5%B0%EA%B3%84#%EC%83%9D%EB%AC%BC_%EA%B5%B0%EA%B3%84_ID) 링크의 리소스 위치 참고) |
| inventory   | [/custominventory](#custominventory) 커맨드로 제작한 인벤토리의 ID                                                                                          |
| message     | JSON                                                                                                                                            |
| command     | Command                                                                                                                                         |
| itemcommand | Command                                                                                                                                         |
| prefix      | [/prefix](#prefix) 커맨드로 제작한 칭호의 ID                                                                                                              |
| required    | [/prefix](#prefix) 커맨드로 제작한 칭호의 ID                                                                                                              |

# Welcome to StackEdit!

Hi! I'm your first Markdown file in **StackEdit**. If you want to learn about StackEdit, you can read me. If you want to play with Markdown, you can edit me. Once you have finished with me, you can create new files by opening the **file explorer** on the left corner of the navigation bar.


# Files

StackEdit stores your files in your browser, which means all your files are automatically saved locally and are accessible **offline!**

## Create files and folders

The file explorer is accessible using the button in left corner of the navigation bar. You can create a new file by clicking the **New file** button in the file explorer. You can also create folders by clicking the **New folder** button.

## Switch to another file

All your files and folders are presented as a tree in the file explorer. You can switch from one to another by clicking a file in the tree.

## Rename a file

You can rename the current file by clicking the file name in the navigation bar or by clicking the **Rename** button in the file explorer.

## Delete a file

You can delete the current file by clicking the **Remove** button in the file explorer. The file will be moved into the **Trash** folder and automatically deleted after 7 days of inactivity.

## Export a file

You can export the current file by clicking **Export to disk** in the menu. You can choose to export the file as plain Markdown, as HTML using a Handlebars template or as a PDF.


# Synchronization

Synchronization is one of the biggest features of StackEdit. It enables you to synchronize any file in your workspace with other files stored in your **Google Drive**, your **Dropbox** and your **GitHub** accounts. This allows you to keep writing on other devices, collaborate with people you share the file with, integrate easily into your workflow... The synchronization mechanism takes place every minute in the background, downloading, merging, and uploading file modifications.

There are two types of synchronization and they can complement each other:

- The workspace synchronization will sync all your files, folders and settings automatically. This will allow you to fetch your workspace on any other device.
  > To start syncing your workspace, just sign in with Google in the menu.

- The file synchronization will keep one file of the workspace synced with one or multiple files in **Google Drive**, **Dropbox** or **GitHub**.
  > Before starting to sync files, you must link an account in the **Synchronize** sub-menu.

## Open a file

You can open a file from **Google Drive**, **Dropbox** or **GitHub** by opening the **Synchronize** sub-menu and clicking **Open from**. Once opened in the workspace, any modification in the file will be automatically synced.

## Save a file

You can save any file of the workspace to **Google Drive**, **Dropbox** or **GitHub** by opening the **Synchronize** sub-menu and clicking **Save on**. Even if a file in the workspace is already synced, you can save it to another location. StackEdit can sync one file with multiple locations and accounts.

## Synchronize a file

Once your file is linked to a synchronized location, StackEdit will periodically synchronize it by downloading/uploading any modification. A merge will be performed if necessary and conflicts will be resolved.

If you just have modified your file and you want to force syncing, click the **Synchronize now** button in the navigation bar.

> **Note:** The **Synchronize now** button is disabled if you have no file to synchronize.

## Manage file synchronization

Since one file can be synced with multiple locations, you can list and manage synchronized locations by clicking **File synchronization** in the **Synchronize** sub-menu. This allows you to list and remove synchronized locations that are linked to your file.


# Publication

Publishing in StackEdit makes it simple for you to publish online your files. Once you're happy with a file, you can publish it to different hosting platforms like **Blogger**, **Dropbox**, **Gist**, **GitHub**, **Google Drive**, **WordPress** and **Zendesk**. With [Handlebars templates](http://handlebarsjs.com/), you have full control over what you export.

> Before starting to publish, you must link an account in the **Publish** sub-menu.

## Publish a File

You can publish your file by opening the **Publish** sub-menu and by clicking **Publish to**. For some locations, you can choose between the following formats:

- Markdown: publish the Markdown text on a website that can interpret it (**GitHub** for instance),
- HTML: publish the file converted to HTML via a Handlebars template (on a blog for example).

## Update a publication

After publishing, StackEdit keeps your file linked to that publication which makes it easy for you to re-publish it. Once you have modified your file and you want to update your publication, click on the **Publish now** button in the navigation bar.

> **Note:** The **Publish now** button is disabled if your file has not been published yet.

## Manage file publication

Since one file can be published to multiple locations, you can list and manage publish locations by clicking **File publication** in the **Publish** sub-menu. This allows you to list and remove publication locations that are linked to your file.


# Markdown extensions

StackEdit extends the standard Markdown syntax by adding extra **Markdown extensions**, providing you with some nice features.

> **ProTip:** You can disable any **Markdown extension** in the **File properties** dialog.


## SmartyPants

SmartyPants converts ASCII punctuation characters into "smart" typographic punctuation HTML entities. For example:

|                |ASCII                          |HTML                         |
|----------------|-------------------------------|-----------------------------|
|Single backticks|`'Isn't this fun?'`            |'Isn't this fun?'            |
|Quotes          |`"Isn't this fun?"`            |"Isn't this fun?"            |
|Dashes          |`-- is en-dash, --- is em-dash`|-- is en-dash, --- is em-dash|


## KaTeX

You can render LaTeX mathematical expressions using [KaTeX](https://khan.github.io/KaTeX/):

The *Gamma function* satisfying $\Gamma(n) = (n-1)!\quad\forall n\in\mathbb N$ is via the Euler integral

$$
\Gamma(z) = \int_0^\infty t^{z-1}e^{-t}dt\,.
$$

> You can find more information about **LaTeX** mathematical expressions [here](http://meta.math.stackexchange.com/questions/5020/mathjax-basic-tutorial-and-quick-reference).


## UML diagrams

You can render UML diagrams using [Mermaid](https://mermaidjs.github.io/). For example, this will produce a sequence diagram:

```mermaid
sequenceDiagram
Alice ->> Bob: Hello Bob, how are you?
Bob-->>John: How about you John?
Bob--x Alice: I am good thanks!
Bob-x John: I am good thanks!
Note right of John: Bob thinks a long<br/>long time, so long<br/>that the text does<br/>not fit on a row.

Bob-->Alice: Checking with John...
Alice->John: Yes... John, how are you?
```

And this will produce a flow chart:

```mermaid
graph LR
A[Square Rect] -- Link text --> B((Circle))
A --> C(Round Rect)
B --> D{Rhombus}
C --> D
```