# cell-line-ancestry
A rudimentary program to determine the possible ancestries of cell lines from a database.

There is a database refence file (.csv) and a query reference file (.csv). Using the query reference file, multiple queries can be submitted at once.
The program will write possible ancestries to a results file that will show the combinations. 

# What are cell lines? How do they have ancestry?
Cell lines are like 'strains' of cells. They are identifiable by things called STR markers. Sometimes, these lines can sort of mix, and their STR markers get scrambled.
For example,
Cell Line A has the marker values TPOX: 4, vWA: 21, and D7S820: 17. (made up values, not realistic)
Cell Line B has the marker values TPOX: 12, vWA: 23, and D7S820: 16.

Oh no! They get mixed, creating cell line C that has scrambled markers!
Cell Line C has marker values TPOX: 12, vWA: 21, and D7S820: 16.
But what two lines mixed to create cell line C? That would be its ancestors, cell lines A and B.
