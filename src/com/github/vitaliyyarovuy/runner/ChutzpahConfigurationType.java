package com.github.vitaliyyarovuy.runner;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * PhoneGapConfigurationType.java
 * <p/>
 * Created by Masahiro Suzuka on 2014/04/04.
 */
public class ChutzpahConfigurationType implements ConfigurationType {
  public ChutzpahConfigurationFactory myConfigurationFactory;

  public ChutzpahConfigurationType() {
    myConfigurationFactory = new ChutzpahConfigurationFactory(this);
  }

  @Override
  public String getDisplayName() {
    return "PhoneGap/Cordova";
  }

  @Override
  public String getConfigurationTypeDescription() {
    return "PhoneGap/Cordova Application";
  }

  @Override
  public Icon getIcon() {
    return null;
  }

  @NotNull
  @Override
  public String getId() {
    return "PhoneGap";
  }

  @Override
  public ConfigurationFactory[] getConfigurationFactories() {
    return new ChutzpahConfigurationFactory[]{myConfigurationFactory};
  }

  public class ChutzpahConfigurationFactory extends ConfigurationFactory {

    public ChutzpahConfigurationFactory(ConfigurationType type) {
      super(type);
    }

    @Override
    public RunConfiguration createTemplateConfiguration(Project project) {
      return new ChutzpahRunConfiguration(project, myConfigurationFactory, "Chutzpah");
    }

    @Override
    public boolean isConfigurationSingletonByDefault() {
      return true;
    }
  }
}
