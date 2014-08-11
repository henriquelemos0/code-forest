code-forest
===========

The CodeForest Repository

Eclipse Plug-in that shows the project code represented as a forest of cacti. The metaphor represent each class as a cacti tree, each method as a branch and each line as leaf (thorns). The position of the tree regarding the whole forest is based on the suspicious value of the class, the class with the node containing the highest value will be in the front row and in the right site. The goal is to focus the programmer’s attention on the highest ranked cacti. The elements are colored also based on the suspicious value, the red color represent the highest ranked elements and green represents the lowest ranked ones, it varies from pure red to pure green as the suspicious value decrease.

Now, integrated with [Jaguar](https://github.com/henriquelemos0/jaguar). Hence, it is possible to run the tests collecting coverage information, generate the xml containing the suspicous value of each elemente and see them as a cacti forest.

Check [Wiki](https://github.com/henriquelemos0/code-forest/wiki) for more information.
