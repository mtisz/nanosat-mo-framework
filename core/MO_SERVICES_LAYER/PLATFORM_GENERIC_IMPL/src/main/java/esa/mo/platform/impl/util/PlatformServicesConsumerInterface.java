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
package esa.mo.platform.impl.util;

import java.io.IOException;
import org.ccsds.moims.mo.platform.autonomousadcs.consumer.AutonomousADCSStub;
import org.ccsds.moims.mo.platform.camera.consumer.CameraStub;
import org.ccsds.moims.mo.platform.gps.consumer.GPSStub;
import org.ccsds.moims.mo.platform.magnetometer.consumer.MagnetometerStub;
import org.ccsds.moims.mo.platform.opticaldatareceiver.consumer.OpticalDataReceiverStub;
import org.ccsds.moims.mo.platform.softwaredefinedradio.consumer.SoftwareDefinedRadioStub;

/**
 *
 * @author Cesar Coelho
 */
public interface PlatformServicesConsumerInterface {

//    public void init(COMServicesProvider comServices) throws MALException;

    public abstract AutonomousADCSStub getAutonomousADCSService() throws IOException;

    public abstract CameraStub getCameraService() throws IOException;

    public abstract GPSStub getGPSService() throws IOException;
    
    public abstract MagnetometerStub getMagnetometerService() throws IOException;
    
    public abstract OpticalDataReceiverStub getOpticalDataReceiverService() throws IOException;

    public abstract SoftwareDefinedRadioStub getSoftwareDefinedRadioService() throws IOException;

}