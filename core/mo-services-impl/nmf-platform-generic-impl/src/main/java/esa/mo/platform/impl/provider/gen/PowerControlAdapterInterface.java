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
package esa.mo.platform.impl.provider.gen;

import java.io.IOException;
import java.util.Map;
import org.ccsds.moims.mo.mal.structures.Identifier;
import org.ccsds.moims.mo.platform.powercontrol.structures.Device;
import org.ccsds.moims.mo.platform.powercontrol.structures.DeviceList;

public interface PowerControlAdapterInterface
{

  public Map<Identifier, Device> getDeviceMap();

  public void enableDevices(DeviceList devices) throws IOException;

}
