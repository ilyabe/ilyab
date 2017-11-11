(ns ilyab.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [ilyab.core-test]))

(doo-tests 'ilyab.core-test)

