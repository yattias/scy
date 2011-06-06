package eu.scy.client.desktop.localtoolbroker.accesschecker;

import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class AccessChecker
{
   public static final String GENERATOR_SAME = "same";
   public static final String GENERATOR_REVERSE = "reverse";
   public static final String GENERATOR_HASH_8 = "hash8";
   private static final String CHARACTERS_HASH_8 = "01234567890ABCDEF"; // need 2^(32/4)=16
                                                                        // characters
   public static final String GENERATOR_HASH_7 = "hash7";
   private static final String CHARACTERS_HASH_7 = "abcdefghijklmnopqrstuvwxyz"; // need 2^(32/7)=24
                                                                                 // characters
   public static final String GENERATOR_HASH_6 = "hash6";
   private static final String CHARACTERS_HASH_6 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNO"; // need
                                                                                                // 2^(32/6)=41
                                                                                                // characters
   public static final String GENERATOR_HASH_5 = "hash5";
   private static final String CHARACTERS_HASH_5 = "abcdefghijkmnopqrstuvwxyzABCDEFGHIJKLMNPQRSTUVWXYZ23456789~!@#$%^&*()_+|-=\\<>?,./[]{}"; // need
                                                                                                                                             // 2^(32/5)=85
                                                                                                                                             // characters
   private NameChecker nameChecker;
   private PasswordGenerator passwordGenerator;
   private List<String> passwordGeneratorNames;
   private Hashtable<String, PasswordGenerator> passwordGenerators;
   private PasswordChecker passwordChecker;
   private List<String> passwordCheckerNames;
   private Hashtable<String, PasswordChecker> passwordCheckers;

   public AccessChecker()
   {
      this(GENERATOR_SAME);
   }

   public AccessChecker(String generatorName)
   {
      passwordGenerators = new Hashtable<String, PasswordGenerator>();
      passwordGeneratorNames = new ArrayList<String>();
      passwordCheckerNames = new ArrayList<String>();
      passwordCheckers = new Hashtable<String, PasswordChecker>();
      addDefaultPasswordGenerators();
      setNameChecker(new NameCheckerJavaIdentifier());
      setPasswordGenerator(generatorName);
      setPasswordChecker(generatorName);
   }

   private void addDefaultPasswordGenerators()
   {
      addPasswordGenerator(GENERATOR_SAME, new PasswordSame());
      addPasswordGenerator(GENERATOR_REVERSE, new PasswordReverse());
      addPasswordGenerator(GENERATOR_HASH_8, new PasswordHash(CHARACTERS_HASH_8));
      addPasswordGenerator(GENERATOR_HASH_7, new PasswordHash(CHARACTERS_HASH_7));
      addPasswordGenerator(GENERATOR_HASH_6, new PasswordHash(CHARACTERS_HASH_6));
      addPasswordGenerator(GENERATOR_HASH_5, new PasswordHash(CHARACTERS_HASH_5));
   }

   public void addPasswordGenerator(String name, PasswordGenerator pwGenerator)
   {
      String lcName = name.toLowerCase();
      passwordGenerators.put(lcName, pwGenerator);
      passwordGeneratorNames.add(lcName);
      addPasswordChecker(name, pwGenerator);
   }

   public void addPasswordChecker(String name, PasswordChecker pwChecker)
   {
      String lcName = name.toLowerCase();
      passwordCheckers.put(lcName, pwChecker);
      passwordCheckerNames.add(lcName);
   }

   public List<String> getPasswordGeneratorNames()
   {
      return passwordGeneratorNames;
   }

   public void setNameChecker(NameChecker nameChecker)
   {
      this.nameChecker = nameChecker;
   }

   public void setPasswordGenerator(PasswordGenerator passwordGenerator)
   {
      this.passwordGenerator = passwordGenerator;
   }

   public void setPasswordGenerator(String generatorName)
   {
      PasswordGenerator newPasswordGenerator = passwordGenerators.get(generatorName.toLowerCase());
      if (newPasswordGenerator == null)
      {
         throw new IllegalArgumentException("Unknown password method: " + generatorName);
      }
      setPasswordGenerator(newPasswordGenerator);
   }

   private NameChecker getNameChecker()
   {
      return nameChecker;
   }

   private PasswordGenerator getPasswordGenerator()
   {
      return passwordGenerator;
   }

   public String getPassword(String userName, String configId)
   {
      String usedUserName = userName;
      if (getNameChecker() != null)
      {
         usedUserName = getNameChecker().checkAndCleanName(usedUserName);
      }
      if (getPasswordGenerator() != null)
      {
         return getPasswordGenerator().getPassword(usedUserName, configId);
      }
      throw new IllegalStateException("No password generator defined");
   }

   public void setPasswordChecker(String name)
   {
      PasswordChecker newPasswordChecker = null;
      if (name != null)
      {
         newPasswordChecker = passwordCheckers.get(name.toLowerCase());
      }
      if (newPasswordChecker == null)
      {
         throw new IllegalArgumentException("Unknown password method: " + name);
      }
      passwordChecker = newPasswordChecker;
   }

   public boolean isAccessAllowed(String userName, String configId, String password)
   {
      if (passwordChecker == null)
         throw new IllegalStateException("No password checker defined");
      return passwordChecker.checkPassword(userName, configId, password);
   }

   public String checkAndCleanName(String userName)
   {
      if (nameChecker == null)
         throw new IllegalStateException("No name checker defined");
      return nameChecker.checkAndCleanName(userName);

   }

}
