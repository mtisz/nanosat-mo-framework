/* ----------------------------------------------------------------------------
 * Copyright (C) 2015      European Space Agency
 *                         European Space Operations Centre
 *                         Darmstadt
 *                         Germany
 * ----------------------------------------------------------------------------
 * System                : ESA NanoSat MO Framework
 * ----------------------------------------------------------------------------
 * Licensed under the European Space Agency Public License, Version 2.0
 * You may not use this file except in compliance with the License.
 *
 * Except as expressly set forth in this License, the Software is provided to
 * You on an "as is" basis and without warranties of any kind, including without
 * limitation merchantability, fitness for a particular purpose, absence of
 * defects or errors, accuracy or non-infringement of intellectual property rights.
 * 
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 * ----------------------------------------------------------------------------
 */
package esa.mo.demo.provider.opssatsimulation3;

import esa.mo.demo.provider.opssatsimulation2.MonitoringParametersImpl;
import esa.mo.helpertools.helpers.HelperMisc;
import esa.mo.helpertools.connections.ConnectionProvider;
import esa.mo.com.impl.util.COMServicesProvider;
import esa.mo.mc.impl.provider.AggregationProviderServiceImpl;
import esa.mo.mc.impl.provider.ParameterManager;
import esa.mo.mc.impl.provider.ParameterProviderServiceImpl;
import opssat.simulator.InstrumentsSimulator;

/**
 * This class provides a simple cli for the control of the provider.
 * 
 */
public class DemoSimulationOPSSAT3
{

  private static final InstrumentsSimulator instrumentsSimulator = new InstrumentsSimulator();
  private static ParameterManager parameterManager;
  
  private static final COMServicesProvider comServices = new COMServicesProvider();

  private static final ParameterProviderServiceImpl parameterService = new ParameterProviderServiceImpl();
  private static final AggregationProviderServiceImpl aggregationService = new AggregationProviderServiceImpl();

  /**
   * Main command line entry point.
   *
   * @param args the command line arguments
   * @throws java.lang.Exception If there is an error
   */
  public static void main(final String args[]) throws Exception
  {

    ConnectionProvider.resetURILinksFile();
    HelperMisc.loadPropertiesFile();

    comServices.init();

    parameterManager = new ParameterManager(comServices, new MonitoringParametersImpl(instrumentsSimulator));
    
    parameterService.init(parameterManager);
    aggregationService.init(comServices, parameterManager);

    
  }

}

