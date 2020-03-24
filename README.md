# Parmorel Eclipse Plugin
This will be a plugin for Eclipse that can fix errors in a model autimatically using the Parmorel algorithm.

## How to download the project
1. Clone the project and its submodules: </br>
  `git clone --recursive https://github.com/MagMar94/ParmorelEclipsePlugin.git` </br>
  If you are not familiar with submodules, you can have a look at [Vogella](https://www.vogella.com/tutorials/GitSubmodules/article.html) or at the [Git-documentation](https://git-scm.com/book/en/v2/Git-Tools-Submodules).
2. Make sure you are running Eclipse Version 2018-12 (4.10.0) or a compatible version, and have installed the Eclipse Modeling Tools. Eclipse-packages can be found [here](https://www.eclipse.org/downloads/packages/).
3. Make sure you have the Plug-in Development Environment.

## How to run the project
1. Once imported to Eclipse, right click the project and select `Run As` -> `Eclipse Application`
2. A new Eclipse instance will open.
3. Create a project in the new Eclipse Instance.
4. Import some ecore-models.
5. Right-click on the models in the Package Explorer and select `Parmorel` -> `Repair`
6. Select some preferences in the pop-up, and clik `OK`
7. In the Select Solution view the possible fixes appear. Click one and compare.
