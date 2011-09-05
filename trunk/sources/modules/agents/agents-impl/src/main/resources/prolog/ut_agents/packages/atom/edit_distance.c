/*  $Id$
 *  
 *  File	edit_distance.c
 *  Part of	Prolog foreign library
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  Purpose	Computes the edit distance of two strings
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  
 *  Notice	Copyright (c) 2007  University of Twente
 *  
 *  History	03/04/07  (Created)
 *  		24/08/11  (Last modified)
 */ 

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

#include <stdlib.h>
#include <stdio.h>
#include <ctype.h>
#include <string.h>
#include <SWI-Prolog.h>


static int
minimum3(int d1, int d2, int d3)
{ if (d1 < d2)
    return d1 < d3 ? d1 : d3;
  return d2 < d3 ? d2 : d3;
}


static int
minimum2(int d1, int d2)
{ return d1 < d2 ? d1 : d2;
}


static int
levenshtein(const char* s1, const char* s2)
{ int len1 = strlen(s1);
  int len2 = strlen(s2);
  int i, j, cost;
  int** d;
  
  d = PL_malloc(sizeof(int*)*(len1+1));
  for (i=0; i<=len1; i++)
    d[i] = PL_malloc(sizeof(int)*(len2+1));

  for (i=0; i<=len1; i++)
    d[i][0] = i;
  for (j=0; j<=len2; j++)
    d[0][j] = j;

  for (j=1; j<=len2; j++)
  { for (i=1; i<=len1; i++)
    { if (s1[i-1] == s2[j-1])
      { d[i][j] = d[i-1][j-1];
	continue;
      }
      d[i][j] = minimum3(d[i-1][j  ] + 1,
                         d[i  ][j-1] + 1,
                         d[i-1][j-1] + 1);
    }
  }

  cost = d[len1][len2];

  for (i=0; i<=len1; i++)
    PL_free(d[i]);
  PL_free(d);

  return cost;
}


static int
damerau(const char* s1, const char* s2)
{ int len1 = strlen(s1);
  int len2 = strlen(s2);
  int i, j, cost;
  int** d;
  
  d = PL_malloc(sizeof(int*)*(len1+1));
  for (i=0; i<=len1; i++)
    d[i] = PL_malloc(sizeof(int)*(len2+1));

  for (i=0; i<=len1; i++)
    d[i][0] = i;
  for (j=0; j<=len2; j++)
    d[0][j] = j;

  for (i=1; i<=len1; i++)
  { for (j=1; j<=len2; j++)
    { cost = (s1[i-1] == s2[j-1] ? 0 : 1);
      d[i][j] = minimum3(d[i-1][j  ] + 1,
                         d[i  ][j-1] + 1,
                         d[i-1][j-1] + cost);
      if (i>1 && j>1 && s1[i-1] == s2[j-1-1] && s1[i-1-1] == s2[j-1])
        d[i][j] = minimum2(d[i][j], d[i-2][j-2]+cost);
    }
  }

  cost = d[len1][len2];

  for (i=0; i<=len1; i++)
    PL_free(d[i]);
  PL_free(d);

  return cost;
}


static foreign_t
levenshtein3(term_t tatom1, term_t tatom2, term_t tdist)
{ char *s1, *s2;

  if (PL_get_atom_chars(tatom1, &s1) && PL_get_atom_chars(tatom2, &s2))
    return PL_unify_integer(tdist, levenshtein(s1,s2));
  return FALSE;
}


static foreign_t
damerau3(term_t tatom1, term_t tatom2, term_t tdist)
{ char *s1, *s2;

  if (PL_get_atom_chars(tatom1, &s1) && PL_get_atom_chars(tatom2, &s2))
    return PL_unify_integer(tdist, damerau(s1,s2));
  return FALSE;
}


install_t
install_edit_distance()
{ PL_register_foreign("edit_distance", 3, levenshtein3, 0);
  PL_register_foreign("damerau_edit_distance", 3, damerau3, 0);
}
