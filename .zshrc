# ZSH + color highlighting
source /usr/share/zsh/5.3/functions/bashcompinit 2> /dev/null
ZSH_THEME="robbyrussell"
plugins=(fast-syntax-highlighting)
source $ZSH/oh-my-zsh.sh
PROMPT='%{$fg_bold[cyan]%}%c%{$reset_color%} '

alias ls="ls --color"
export SDKMAN_DIR="/Users/npeters/.sdkman"

# run
[ -f .autorun.sh ] && source .autorun.sh
