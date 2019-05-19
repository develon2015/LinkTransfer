## config.properties

> transfer = /static --> down.jsp的重定向目录, Transfer.java中`SystemUtil.getConfigParam("transfer") + "/" + file.getName();`<br>
> static_root_directory = /home/d/files --> 下载文件的存放目录

## nginx 配置

```
        server {
                listen [::]:80;
                server_name down.deve.cf;

                location / {
                        proxy_pass http://tomcat/LinkTransfer/;
                }

                location ~ /static/(.*) {
                        root /home/d/files;
                        try_files /$1 =404;
                }
        }
```
