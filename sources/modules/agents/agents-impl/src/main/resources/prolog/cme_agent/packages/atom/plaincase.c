/*  $Id$
 *  
 *  File	plaincase.c
 *  Part of	Prolog packages
 *  Author	Anjo Anjewierden, a.a.anjewierden@utwente.nl
 *  		Anjo Anjewierden, anjo@science.uva.nl
 *  Purpose	Tokenisation
 *  Works with	SWI-Prolog (www.swi-prolog.org)
 *  
 *  Notice	Copyright (c) 2007-2011  University of Twente
 *		Copyright (c) 2001-2006  University of Amsterdam
 *  
 *  History	19/10/01  (Created)
 *  		11/10/11  (Last modified)
 */ 

/*------------------------------------------------------------
 *  Directives
 *------------------------------------------------------------*/

#include <stdlib.h>
#include <stdio.h>
#include <ctype.h>
#include <wctype.h>
#include <string.h>
#include <SWI-Prolog.h>
#include <locale.h>
#ifdef HAVE_MALLOC_H
#include <malloc.h>
#endif

#include "plaincase.h"


/*------------------------------------------------------------
 *  Prototypes
 *------------------------------------------------------------*/

static int	is_plaincase(const char*);
static int	is_diacritics(const unsigned char*);

static int	is_wplaincase(const wchar_t*, unsigned int);
static int	is_wdiacritics(const wchar_t*, unsigned int);

static void	no_diacritics(const char*, char*);

static void	wno_diacritics(const wchar_t*, unsigned int, wchar_t*);


/*------------------------------------------------------------
 *  Constants
 *------------------------------------------------------------*/

functor_t	FUNCTOR_word1;
functor_t	FUNCTOR_character1;
functor_t	FUNCTOR_space1;
functor_t	FUNCTOR_integer1;
functor_t	FUNCTOR_newline1;


/*------------------------------------------------------------
 *  Prototypes
 *------------------------------------------------------------*/

unsigned char	latin1_diacritics[256] =
{   0,   1,   2,   3,   4,   5,   6,   7,   8,   9,
   10,  11,  12,  13,  14,  15,  16,  17,  18,  19,
   20,  21,  22,  23,  24,  25,  26,  27,  28,  29,
   30,  31,  32,  33,  34,  35,  36,  37,  38,  39,
   40,  41,  42,  43,  44,  45,  46,  47,  48,  49,
   50,  51,  52,  53,  54,  55,  56,  57,  58,  59,
   60,  61,  62,  63,  64,  65,  66,  67,  68,  69,
   70,  71,  72,  73,  74,  75,  76,  77,  78,  79,
   80,  81,  82,  83,  84,  85,  86,  87,  88,  89,
   90,  91,  92,  93,  94,  95,  96,  97,  98,  99,
  100, 101, 102, 103, 104, 105, 106, 107, 108, 109,
  110, 111, 112, 113, 114, 115, 116, 117, 118, 119,
  120, 121, 122, 123, 124, 125, 126, 127, 128, 129,
  130, 131, 132, 133, 134, 135, 136, 137, 138, 139,
  140, 141, 142, 143, 144, 145, 146, 147, 148, 149,
  150, 151, 152, 153, 154, 155, 156, 157, 158, 159,
  160, 161, 162, 163, 164, 165, 166, 167, 168, 169,
  170, 171, 172, 173, 174, 175, 176, 177, 178, 179,
  180, 181, 182, 183, 184, 185, 186, 187, 188, 189,
  190, 191,  65,  65,  65,  65,  65,  65,  65,  67,
   69,  69,  69,  69,  73,  73,  73,  73,  68,  78,
   79,  79,  79,  79,  79, 215,  79,  85,  85,  85,
   85,  89,  84,  83,  97,  97,  97,  97,  97,  97,
   97,  99, 101, 101, 101, 101, 105, 105, 105, 105,
  100, 110, 111, 111, 111, 111, 111, 247, 111, 117,
  117, 117, 117, 121, 116, 121
};


/*------------------------------------------------------------
 *  Checking character classes
 *------------------------------------------------------------*/

static int
is_lower_token(int c)
{ if (islower(c))
    return 1;
  if (islower(latin1_diacritics[c]))
    return 1;
  return 0;
}


static int
is_wlower_token(int c)
{ if (iswlower(c))
    return 1;
  if (c < 256 && islower(latin1_diacritics[c]))
    return 1;
  return 0;
}


/*------------------------------------------------------------
 *  Case conversion
 *------------------------------------------------------------*/

static int
is_plaincase(const char* s)
{ while (*s)
  { if (!is_lower_token(*s))
      return 0;
    if (*s > 'z')
      return 0;
    s++;
  }
  return 1;
}


static int
is_wplaincase(const wchar_t* ws, unsigned int len)
{ while (len--)
  { if (!is_wlower_token(*ws))
      return 0;
    ws++;
  }
  return 1;
}


static int
is_diacritics(const unsigned char* s)
{ while (*s)
  { if (*s != latin1_diacritics[*s])
      return 1;
    s++;
  }
  return 0;
}


static int
is_wdiacritics(const wchar_t* ws, unsigned int len)
{ while (len--)
  { if (*ws < 256 && *ws != latin1_diacritics[*ws])
      return 1;
    ws++;
  }
  return 0;
}


void
no_diacritics(const char* s, char* t)
{ unsigned char* u = (unsigned char*) s;
  while (*u)
    *t++ = latin1_diacritics[*u++];
  *t = '\0';
}


void
wno_diacritics(const wchar_t* ws, unsigned int len, wchar_t* t)
{ while (len--)
  { if (*ws < 256)
      *t = latin1_diacritics[*ws];
    t++, ws++;
  }
  *t = '\0';
}


void
latin1_html(const unsigned char* s, unsigned char* t)
{ for (; *s; s++)
  { if (*s > 127)
    { *t++ = '&';
      *t++ = '#';
      *t++ = '0' + (*s / 100);
      *t++ = '0' + ((*s/10) % 10);
      *t++ = '0' + (*s % 10);
      *t++ = ';';
    } else
      *t++ = *s;
  }
}


/*------------------------------------------------------------
 *  Prolog interface
 *------------------------------------------------------------*/

static void
init_constants()
{ FUNCTOR_word1     = PL_new_functor(PL_new_atom("word"), 1);
  FUNCTOR_space1    = PL_new_functor(PL_new_atom("space"), 1);
  FUNCTOR_integer1  = PL_new_functor(PL_new_atom("integer"), 1);
  FUNCTOR_character1= PL_new_functor(PL_new_atom("character"), 1);
  FUNCTOR_newline1  = PL_new_functor(PL_new_atom("newline"), 1);
}


/*------------------------------------------------------------
 *  Interface functions
 *------------------------------------------------------------*/

static foreign_t
is_plaincase1(term_t atom)
{ char* s;
  wchar_t* ws;
  size_t len;
  
  if (PL_get_chars(atom, &s, CVT_ATOMIC))
    return is_plaincase(s);
  if (PL_get_wchars(atom, &len, &ws, CVT_ATOMIC))
    return is_wplaincase(ws, len);
  return FALSE;
}

static foreign_t
is_diacritics1(term_t atom)
{ char* s;
  wchar_t* ws;
  size_t len;
  
  if (PL_get_chars(atom, &s, CVT_ATOMIC))
    return is_diacritics((unsigned char*) s);
  if (PL_get_wchars(atom, &len, &ws, CVT_ATOMIC))
    return is_wdiacritics(ws, len);
  return FALSE;
}


static foreign_t
pl_no_diacritics_atom(term_t in, term_t out)
{ char *s, *to;
  size_t len;
  wchar_t *ws, *wto;
  
  if (PL_get_nchars(in, &len, &s, CVT_ATOMIC))
  { if ((to = alloca(len+1)))
    { no_diacritics(s, to);
      return PL_unify_chars(out, PL_ATOM, len, to);
    }
  } else
  if (PL_get_wchars(in, &len, &ws, CVT_ATOMIC))
  { if ((wto = alloca((len+1)*sizeof(wchar_t))))
    { wno_diacritics(ws, len, wto);
      return PL_unify_wchars(out, PL_ATOM, len, wto);
    }
  }

  return FALSE;
}


static foreign_t
plaincase_atom2(term_t in, term_t out)
{ return plaincase_term(in, out);
}


/*------------------------------------------------------------
 *  Installation
 *------------------------------------------------------------*/

install_t
install_plaincase()
{ init_constants();

  setlocale(LC_COLLATE, "en_GB.UTF-8");

  PL_register_foreign("is_plaincase", 1, is_plaincase1, 0);
  PL_register_foreign("is_diacritics", 1, is_diacritics1, 0);

  PL_register_foreign("plaincase_atom", 2, plaincase_atom2, 0);

  PL_register_foreign("no_diacritics_atom", 2, pl_no_diacritics_atom, 0);
}
