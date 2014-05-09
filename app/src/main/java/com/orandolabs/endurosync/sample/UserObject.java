package com.orandolabs.endurosync.sample;

import com.orandolabs.endurosync.EnduroException;
import com.orandolabs.endurosync.EnduroObject;
import com.orandolabs.endurosync.EnduroClassAnnotation;
import com.orandolabs.endurosync.EnduroPropertyAnnotation;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

@EnduroClassAnnotation(className = "user")
public class UserObject extends EnduroObject
{
    public UserObject()
    {
    }

    @EnduroPropertyAnnotation(propertyName = "email")
    public String email;
    public String getEmail()
    {
        return toString("email");
    }
    public void setEmail(String email)
    {
        setValue("email", email);
    }

    @EnduroPropertyAnnotation(propertyName = "age")
    public int age;
    public int getAge()
    {
        return toInt("age");
    }
    public void setAge(int age)
    {
        setValue("age", age);
    }

    @EnduroPropertyAnnotation(propertyName = "birthday")
    public Date birthday;
    public Date getBirthday()
    {
        return toDate("birthday");
    }
    public void setBirthday(Date birthday)
    {
        setValue("birthday", birthday);
    }

    @EnduroPropertyAnnotation(propertyName = "links")
    public HashSet<LinkObject> links;

    public HashSet<LinkObject> getLinks()
    {
        if (links == null) {
            List<LinkObject> ls = toObjectList("links", LinkObject.class);
            links = new HashSet<LinkObject>();
            // This deletes dupes
            for (LinkObject l : ls)
            {
                if (links.contains(l))
                    this.objectStore.deleteObject(l);
                else
                    links.add(l);
            }
        }
        return links;
    }

    public void addLink(LinkObject link) throws EnduroException
    {
        addObjectValue("links", link);
        links.add(link);
    }

    public void removeLink(LinkObject link) throws EnduroException
    {
        removeObjectValue("links", link);
        links.add(link);
    }
}
