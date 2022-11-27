library(httpgd)
library(xtable)

plot_svg <- function(code,name){
    pic <- hgd_inline(code)
    cat(paste0("\n---start---SVG\n",pic,"\n---end---SVG-",name,"\n"),file=tempOutputFile,append=T)
}

saveTxt <- function(txt,name){
    cat(paste0("\n---start---SVG\n",txt,"\n---end---SVG-",name,"\n"),file=tempOutputFile,append=T)
}

saveTable <- function(tab,name){
    out <-  xtable(tab,label = name,caption = name) |>
        print(type="html")
    cat(paste0("\n---start---SVG\n",out,"\n---end---SVG-",name,"\n"),file=tempOutputFile,append=T)
}

