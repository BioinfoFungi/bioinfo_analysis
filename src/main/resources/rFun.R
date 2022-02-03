library(httpgd)
plot_svg <- function(code){
    pic <- hgd_inline(code)
    cat(paste0("\n---start---\n",pic,"\n---end---\n"),file=tempOutputFile,append=T)
}