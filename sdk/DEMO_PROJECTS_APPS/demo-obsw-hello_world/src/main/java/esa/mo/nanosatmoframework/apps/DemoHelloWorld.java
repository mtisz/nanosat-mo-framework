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
package esa.mo.nanosatmoframework.apps;

import esa.mo.helpertools.connections.ConnectionProvider;
import esa.mo.helpertools.helpers.HelperAttributes;
import esa.mo.helpertools.helpers.HelperMisc;
import esa.mo.nanosatmoframework.MCRegistration;
import esa.mo.nanosatmoframework.MonitorAndControlNMFAdapter;
import esa.mo.nanosatmoframework.SimpleMonitorAndControlAdapter;
import esa.mo.nanosatmoframework.NanoSatMOFrameworkInterface;
import esa.mo.nanosatmoframework.provider.NanoSatMOMonolithicSim;
import java.io.Serializable;
import org.ccsds.moims.mo.mal.provider.MALInteraction;
import org.ccsds.moims.mo.mal.structures.Attribute;
import org.ccsds.moims.mo.mal.structures.Duration;
import org.ccsds.moims.mo.mal.structures.Identifier;
import org.ccsds.moims.mo.mal.structures.UInteger;
import org.ccsds.moims.mo.mal.structures.Union;
import org.ccsds.moims.mo.mc.parameter.structures.ParameterDefinitionDetails;
import org.ccsds.moims.mo.mc.parameter.structures.ParameterDefinitionDetailsList;
import org.ccsds.moims.mo.mc.structures.AttributeValueList;

/**
 * This class provides a simple Hello World demo cli provider
 *
 */
public class DemoHelloWorld {

    private final NanoSatMOFrameworkInterface nanoSatMOFramework = new NanoSatMOMonolithicSim(new MCAdapterSimple());
    private static final String PARAMETER_HELLO = "A_Parameter";
    private String str = "Hello World!";
//    public final COMServicesProvider comServices = new COMServicesProvider();

    public DemoHelloWorld() {

        ConnectionProvider.resetURILinksFile(); // Resets the providerURIs.properties file
        HelperMisc.loadPropertiesFile(); // Loads: provider.properties; settings.properties; transport.properties
        /*
        try {
            this.comServices.init();
        } catch (MALException ex) {
            Logger.getLogger(DemoHelloWorld.class.getName()).log(Level.SEVERE, null, ex);
        }
         */
    }

    /**
     * Main command line entry point.
     *
     * @param args the command line arguments
     * @throws java.lang.Exception If there is an error
     */
    public static void main(final String args[]) throws Exception {
        DemoHelloWorld demo = new DemoHelloWorld();
    }

    public class MCAdapter extends MonitorAndControlNMFAdapter {

        @Override
        public void initialRegistrations(MCRegistration registrationObject) {
            registrationObject.setMode(MCRegistration.RegistrationMode.DONT_UPDATE_IF_EXISTS);

            // ------------------ Parameters ------------------
            ParameterDefinitionDetailsList defsOther = new ParameterDefinitionDetailsList();

            defsOther.add(new ParameterDefinitionDetails(
                    new Identifier(PARAMETER_HELLO),
                    "The ADCS mode operation",
                    Union.STRING_SHORT_FORM.byteValue(),
                    "",
                    false,
                    new Duration(3),
                    null,
                    null
            ));

            registrationObject.registerParameters(defsOther);

        }

        @Override
        public Attribute onGetValue(Identifier identifier, Byte rawType) {
            if (PARAMETER_HELLO.equals(identifier.getValue())) {
                return (Attribute) HelperAttributes.javaType2Attribute(str);
            }

            return null;
        }

        @Override
        public Boolean onSetValue(Identifier identifier, Attribute value) {
            if (PARAMETER_HELLO.equals(identifier.getValue())) {
                str = value.toString(); // Let's set the str variable
                return true;  // to confirm that the variable was set                
            }

            return false;
        }

        @Override
        public UInteger actionArrived(Identifier name, AttributeValueList attributeValues, Long actionInstanceObjId, boolean reportProgress, MALInteraction interaction) {
            return null;  // Action service not integrated
        }

    }

    public class MCAdapterSimple extends SimpleMonitorAndControlAdapter {

        @Override
        public boolean actionArrivedSimple(String name, Serializable[] values, Long actionInstanceObjId) {
            return false;
        }

        @Override
        public Serializable onGetValueSimple(String name) {
            if (PARAMETER_HELLO.equals(name)) {
                return str;
            }

            return null;
        }

        @Override
        public boolean onSetValueSimple(String name, Serializable value) {
            if (PARAMETER_HELLO.equals(name)) {
                str = value.toString(); // Let's set the str variable
                return true;  // to confirm that the variable was set
            }

            return false;
        }
    }

}
