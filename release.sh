#!/usr/bin/env bash
# $Id$

mvn -T 1 -B -P+release release:prepare release:perform
