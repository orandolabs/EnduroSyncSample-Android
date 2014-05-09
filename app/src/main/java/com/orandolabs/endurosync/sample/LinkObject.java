package com.orandolabs.endurosync.sample;

import com.orandolabs.endurosync.EnduroClassAnnotation;
import com.orandolabs.endurosync.EnduroObject;
import com.orandolabs.endurosync.EnduroPropertyAnnotation;

@EnduroClassAnnotation(className = "link")
public class LinkObject extends EnduroObject
{
    public LinkObject()
    {
    }

    @EnduroPropertyAnnotation(propertyName = "title")
    public String title;
    public String getTitle()
    {
        return toString("title");
    }
    public void setTitle(String title)
    {
        setValue("title", title);
    }

    @EnduroPropertyAnnotation(propertyName = "url")
    public String url;
    public String getUrl()
    {
        return toString("url");
    }
    public void setUrl(String url)
    {
        setValue("url", url);
    }
}
