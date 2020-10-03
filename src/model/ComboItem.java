package model;

public class ComboItem
{
    private String title;
    private String prefix;

    public ComboItem(String title, String prefix)
    {
        this.title = title;
        this.prefix = prefix;
    }

    @Override
    public String toString()
    {
        return title;
    }

    public String getTitle()
    {
        return title;
    }

    public String getPrefix()
    {
        return prefix;
    }
}