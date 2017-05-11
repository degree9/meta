<pre align="center">
      ___           ___           ___          ___      
     /\  \         /\__\         /\__\        /\  \     
    |::\  \       /:/ _/_       /:/  /       /::\  \    
    |:|:\  \     /:/ /\__\     /:/__/       /:/\:\  \   
  __|:|\:\  \   /:/ /:/ _/_   /::\  \      /:/ /::\  \  
 /::::|_\:\__\ /:/_/:/ /\__\ /:/\:\  \    /:/_/:/\:\__\ 
 \:\--\  \/__/ \:\/:/ /:/  / \/__\:\  \   \:\/:/  \/__/ 
  \:\  \        \::/_/:/  /       \:\  \   \::/__/      
   \:\  \        \:\/:/  /         \:\  \   \:\  \      
    \:\__\        \::/  /           \:\__\   \:\__\     
     \/__/         \/__/             \/__/    \/__/     
</pre>

# [meta] [![Clojars Project][7]][8] [![CircleCI][9]][10] [![Downloads][13]][14]
<!---[![Dependencies Status][11]][12]--->
A stack. A framework. A conversation.

<img src="https://raw.githubusercontent.com/cljs/logo/master/cljs.png" width="100" alt="CLJS Logo"><img src="http://boot-clj.com/assets/images/logos/boot-logo-3.png" width="100" alt="Boot Logo"><img src="http://hoplon.io/images/logos/hoplon-logo.png" width="100" alt="Hoplon Logo"><img src="http://s32.postimg.org/4k9q912x1/aar_QEIys.jpg" width="100" alt="Feathers.js Logo"><img src="https://cdn.worldvectorlogo.com/logos/nodejs-icon.svg" width="90" alt="Node.js Logo"><img src="https://camo.githubusercontent.com/79904b8ba0d1bce43022bbd5710f0ea1db33f54f/68747470733a2f2f7261776769742e636f6d2f73696e647265736f726875732f617765736f6d652d656c656374726f6e2f6d61737465722f656c656374726f6e2d6c6f676f2e737667" width="90" alt="Electron Logo"><img src="https://upload.wikimedia.org/wikipedia/commons/thumb/8/80/Atom_editor_logo.svg/2000px-Atom_editor_logo.svg.png" width="100" alt="Atom Logo"><img src="https://cdn.worldvectorlogo.com/logos/docker.svg" width="100" alt="Docker Logo">

***

## What is [meta]? ##
[meta] is a stack for building tech startups using Clojure(Script).

## Why [meta]? ##
We built [meta] out of the idea that Open Source could be used to build a startup. We named it [meta] as a reminder that software takes many forms and like ideas, should continuously evolve. [meta] is an attempt at building the quickest path from idea to startup.

## When should I use [meta]? ##
[Always.](https://twitter.com/degree9io/status/848972601091346433)

***

<img src="https://raw.githubusercontent.com/cljs/logo/master/cljs.png" width="180" alt="CLJS Logo" align="left">

## [meta] Internals ##
[meta] is written in Clojure(Script) and built using:
- [Boot][1] - Build tooling for Clojure.
- [Hoplon][2] - ClojureScript Web Framework.
- [Feathers.js][3] - An open source REST and realtime API layer for modern applications.
- [Node.js][4] - JavaScript runtime built on Chrome's V8 JavaScript engine.
- [Electron][5] - Build cross platform desktop apps with JavaScript, HTML, and CSS.
- [Atom][6] - A hackable text editor for the 21st Century.

***

<img src="http://boot-clj.com/assets/images/logos/boot-logo-3.png" width="180" alt="Boot Logo" align="right">

## Using [meta] ... with Boot [![Boot][24]][1] [![Wiki][34]][35] ##
[meta] is powered by Boot, an environment for building applications using clojure.
To use [meta] `require` it in your build.boot file.
Thanks to Boot, [meta] is built with itself.

```clojure
(set-env! :dependencies [[degree9/meta "0.1.0-SNAPSHOT"]])
(require '[meta.boot :refer :all])

(initialize)
```

- [boot-welcome][36] - ASCII art banners with boot-clj.
- [boot-exec][15] - Boot-clj external process execution using Apache Commons Exec.
- [boot-semver][17] - Semantic Versioning for boot projects.
- [boot-semgit][18] - Semantic Git access from boot tasks.

[![Clojars Project](https://img.shields.io/clojars/v/degree9/boot-welcome.svg)](https://clojars.org/degree9/boot-welcome)
[![Clojars Project](https://img.shields.io/clojars/v/degree9/boot-exec.svg)](https://clojars.org/degree9/boot-exec)
[![Clojars Project](https://img.shields.io/clojars/v/degree9/boot-semver.svg)](https://clojars.org/degree9/boot-semver)
[![Clojars Project](https://img.shields.io/clojars/v/degree9/boot-semgit.svg)](https://clojars.org/degree9/boot-semgit)

***

<img src="http://hoplon.io/images/logos/hoplon-logo.png" width="220" alt="Hoplon Logo" align="left">

## ClojureScript in the Browser ... with Hoplon [![Hoplon][23]][2] ##
We believe in building universal technologies and the web is the most accessible platform available. [meta] builds upon Hoplon to provide abstractions for the DOM.

```clojure
(page "index.html")

(html
  (head
    (title "[meta]"))
  (body
    (p "Hello!")))
```

- [material-hl][32] - Material Design Components for Hoplon

[![Clojars Project](https://img.shields.io/clojars/v/degree9/material-hl.svg)](https://clojars.org/degree9/material-hl)

***

<img src="http://s32.postimg.org/4k9q912x1/aar_QEIys.jpg" width="180" alt="Feathers.js Logo" align="right">

## Realtime Communication ... with Feathers.js [![Feathers][31]][3] ##
[meta] provides both REST and Realtime communication via FeatherScript, a wrapper around Feathers.js implemented in ClojureScript. Feathers enables event-based communication between clients and servers and other API's including persistent data stores.

- [featherscript][20] - A feathers.js wrapper for ClojureScript.

[![Clojars Project](https://img.shields.io/clojars/v/degree9/featherscript.svg)](https://clojars.org/degree9/featherscript)

***

<img src="https://cdn.worldvectorlogo.com/logos/nodejs-icon.svg" width="180" alt="Node.js Logo" align="left">

## ClojureScript on the Server ... with Node.js [![Node][29]][4] ##
[meta] is built entirely out of Clojure(Script) including the backend. This means compiling to JavaScript and running on Node.js.

- [boot-nodejs][16] - Node.js tasks for boot-clj.
- [boot-npm][19] - Node Package Manager (NPM) wrapper task for boot-clj.
- [nodejs-cljs][21] - CLJS bindings for the Node.js API.

[![Clojars Project](https://img.shields.io/clojars/v/degree9/boot-nodejs.svg)](https://clojars.org/degree9/boot-nodejs)
[![Clojars Project](https://img.shields.io/clojars/v/degree9/boot-npm.svg)](https://clojars.org/degree9/boot-npm)
[![Clojars Project](https://img.shields.io/clojars/v/degree9/nodejs-cljs.svg)](https://clojars.org/degree9/nodejs-cljs)

***

<img src="https://camo.githubusercontent.com/79904b8ba0d1bce43022bbd5710f0ea1db33f54f/68747470733a2f2f7261776769742e636f6d2f73696e647265736f726875732f617765736f6d652d656c656374726f6e2f6d61737465722f656c656374726f6e2d6c6f676f2e737667" width="180" alt="Electron Logo" align="right">

## ClojureScript on the Client ... with Electron [![Electron][30]][5] ##
[meta] can package Client/Server applications with Electron. Building cross-platform Clojure(Script) applications.

- [electron-cljs][22] - Electron bindings for CLJS.
- [boot-electron][33] - Electron tasks for boot-clj.

[![Clojars Project](https://img.shields.io/clojars/v/degree9/electron-cljs.svg)](https://clojars.org/degree9/electron-cljs)
[![Clojars Project](https://img.shields.io/clojars/v/degree9/boot-electron.svg)](https://clojars.org/degree9/boot-electron)

***

<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/8/80/Atom_editor_logo.svg/2000px-Atom_editor_logo.svg.png" width="180" alt="Atom Logo" align="left">

## Developed ... with Atom [![Atom][25]][26] ##
[meta] is being cultivated within Atom, a hackable text editor
for the 21st Century.

- [proto-repl][28] - A Clojure Development Environment package for Atom.

[![Clojars Project](https://img.shields.io/clojars/v/proto-repl.svg)](https://clojars.org/proto-repl)

***

<img src="https://cdn.worldvectorlogo.com/logos/docker.svg" width="180" alt="Docker Logo" align="right">

## Containerization ... with Docker ##
[meta] server applications can be containerized for scalability through docker.

- [boot-docker][27] - Docker wrapper for boot-clj.

[![Clojars Project](https://img.shields.io/clojars/v/degree9/boot-docker.svg)](https://clojars.org/degree9/boot-docker)

***

<img src="https://www.gitcheese.com/images/logo.svg" width="180" alt="GitCheese" align="left">

## Donations ... with Cheese ##
If you would like to support this project give us a :star: or consider donating some cheese.

[![gitcheese.com](https://s3.amazonaws.com/gitcheese-ui-master/images/badge.svg)](https://www.gitcheese.com/donate/users/147004/repos/82129902)

***

<img src="https://storage.googleapis.com/material-icons/external-assets/v4/icons/svg/ic_copyright_black_24px.svg" width="160" alt="Info" align="right">

## Appreciation ... with Credits ##
[meta] would not exist without the fine work of all the technologies mentioned and their respective communities. We encourage you to join one.

- [x] All images/logo's are owned by their respective projects/communities.

***

[1]: http://boot-clj.com
[2]: http://hoplon.io
[3]: http://feathersjs.com
[4]: http://nodejs.org
[5]: http://electron.atom.io
[6]: http://atom.io
[7]: https://img.shields.io/clojars/v/degree9/meta.svg
[8]: https://clojars.org/degree9/meta
[9]: https://circleci.com/gh/degree9/meta.svg?style=shield
[10]: https://circleci.com/gh/degree9/meta
[11]: https://jarkeeper.com/degree9/meta/status.svg
[12]: https://jarkeeper.com/degree9/meta
[13]: https://jarkeeper.com/degree9/meta/downloads.svg
[14]: https://jarkeeper.com/degree9/meta
[15]: https://github.com/degree9/boot-exec
[16]: https://github.com/degree9/boot-nodejs
[17]: https://github.com/degree9/boot-semver
[18]: https://github.com/degree9/boot-semgit
[19]: https://github.com/degree9/boot-npm
[20]: https://github.com/degree9/featherscript
[21]: https://github.com/degree9/nodejs-cljs
[22]: https://github.com/degree9/electron-cljs
[23]: https://img.shields.io/badge/hoplon-v7.0.0--SNAPSHOT-orange.svg
[24]: https://img.shields.io/github/release/boot-clj/boot.svg?colorB=dfb317&label=boot
[25]: https://img.shields.io/github/release/atom/atom.svg?label=atom
[26]: https://atom.io
[27]: https://github.com/degree9/boot-docker
[28]: https://github.com/jasongilman/proto-repl
[29]: https://img.shields.io/badge/node-v7.7.4-yellowgreen.svg
[30]: https://img.shields.io/badge/electron-v1.6.2-blue.svg
[31]: https://img.shields.io/badge/feathers-v2.1.1-lightgrey.svg
[32]: https://github.com/degree9/material-hl
[33]: https://github.com/degree9/boot-electron
[34]: https://img.shields.io/badge/wiki-boot-lightgrey.svg
[35]: https://github.com/degree9/meta/wiki/Boot
[36]: https://github.com/degree9/boot-welcome
