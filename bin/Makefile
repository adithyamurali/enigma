# This Makefile is just a convenience.  It bounces all requests to the Makefile
# in the parent directory.

.PHONY: default style check clean

SHELL = bash

default:
	$(MAKE) -C .. default

check:
	$(MAKE) -C .. check

clean:
	$(MAKE) -C .. clean

style:
	$(MAKE) -C .. style

