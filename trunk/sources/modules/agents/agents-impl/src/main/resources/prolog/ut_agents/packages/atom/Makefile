#   $Id$
#   
#   File	Makefile
#   Part of	Utilities package
#   Author	Anjo Anjewierden, anjo@science.uva.nl
#   Purpose	Makefile
#   Works with	gcc
#   
#   Notice	Copyright (c) 1999-2006  University of Amsterdam
#   
#   History	22/06/99  (Created)
#		12/11/05  (Deal with MacOS; JW)
#   		02/03/06  (Last modified)

.SUFFIXES:	.cpp .xml

UNAME=		$(shell uname)
ifeq ($(UNAME), Darwin)
DEFS=
else
DEFS=		-DHAVE_MALLOC_H
endif
PL=		swipl

SO=		$(shell eval `$(PL) -dump-runtime-variables` && echo $$PLSOEXT)
CFLAGS=		-O3 -Wall -fPIC $(DEFS)
#CFLAGS=	-g $(DEFS)

CC=		swipl-ld
LD =		swipl-ld -shared

LIBs =		edit_distance.$(SO)

all:		$(LIBs)

edit_distance.$(SO):	edit_distance.o
		$(LD) -o edit_distance edit_distance.o

.c.o:		$*.c
		$(CC) $(CFLAGS) -c $*.c

# Support

clean:		
		rm -f *.o *.ps *.zip $(LIBs)

install:	
		mv $(LIBs) bin
