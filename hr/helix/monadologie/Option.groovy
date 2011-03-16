package hr.helix.monadologie

interface Option {}

class Some implements Option { def value }

class None implements Option {}
