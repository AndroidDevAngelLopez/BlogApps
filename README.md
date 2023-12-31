<img src="blogaaap.png">

# My Blog App

written in jetpack compose(frontend) and kotlin for backend
kobweb is built on top of ktor and compose web.
compose web uses kotlin multiplatform.

deployed by Render, check this link for app deployed : https://blogmultiplatform-mip0.onrender.com

The web application is divided into two parts, the first a page so that any user can see the latest posts published by the administrator, in the same way, they can search and display by ategories the notes to finally leave your email and not miss out on more content that you like.

<img src="screen13.png" height='100%' width='50%' ><img src="screen12.png"  height='100%' width='50%'><img src="screen11.png" height='100%' width='50%'><img src="screen10.png" height='100%' width='50%'>
<img src="screen9.png" height='100%' width='50%'><img src="screen8.png" height='100%' width='50%'><img src="screen7.png">

The second part is that of the administrator, where the administrator can create notes, assign a category, a level of importance, and write its description with a rich text entry.
Finally you can add images to the note, you can search within your own notes and delete notes.
There is also support for multi administrator.

<img src="screen6.png" height='100%' width='50%' ><img src="screen5.png"  height='100%' width='50%'><img src="screen4.png" height='100%' width='50%'><img src="screen3.png" height='100%' width='50%'>
<img src="screen2.png">

How to run this project :

This is a [Kobweb](https://github.com/varabyte/kobweb) project bootstrapped with the `app/empty` template.

This template is useful if you already know what you're doing and just want a clean slate. By default, it
just creates a blank home page (which prints to the console so you can confirm it's working)

If you are still learning, consider instantiating the `app` template (or one of the examples) to see actual,
working projects.

## Getting Started

First, run the development server by typing the following command in a terminal under the `site` folder:

```bash
$ cd site
$ kobweb run
```

Open [http://localhost:8080](http://localhost:8080) with your browser to see the result.

You can use any editor you want for the project, but we recommend using **IntelliJ IDEA Community Edition** downloaded
using the [Toolbox App](https://www.jetbrains.com/toolbox-app/).

Press `Q` in the terminal to gracefully stop the server.

### Live Reload

Feel free to edit / add / delete new components, pages, and API endpoints! When you make any changes, the site will
indicate the status of the build and automatically reload when ready.

## Exporting the Project

When you are ready to ship, you should shutdown the development server and then export the project using:

```bash
kobweb export
```

When finished, you can run a Kobweb server in production mode:

```bash
kobweb run --env prod
```

If you want to run this command in the Cloud provider of your choice, consider disabling interactive mode since nobody
is sitting around watching the console in that case anyway. To do that, use:

```bash
kobweb run --env prod --notty
```

Kobweb also supports exporting to a static layout which is compatible with static hosting providers, such as GitHub
Pages, Netlify, Firebase, any presumably all the others. You can read more about that approach here:
https://bitspittle.dev/blog/2022/staticdeploy
