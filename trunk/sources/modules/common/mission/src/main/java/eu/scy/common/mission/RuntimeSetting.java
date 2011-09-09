package eu.scy.common.mission;

public class RuntimeSetting
{
   private RuntimeSettingKey key;
   private String value;

   @Override
   public String toString()
   {
      return "RuntimeSetting{" + "key=" + key + "value=" + value + '}';
   }

   public RuntimeSetting()
   {
   }

   public RuntimeSetting(RuntimeSettingKey key, String value)
   {
      this.key = key;
      this.value = value;
   }

   public RuntimeSettingKey getKey()
   {
      return key;
   }

   public void setKey(RuntimeSettingKey key)
   {
      this.key = key;
   }

   public String getValue()
   {
      return value;
   }

   public void setValue(String value)
   {
      this.value = value;
   }

}
