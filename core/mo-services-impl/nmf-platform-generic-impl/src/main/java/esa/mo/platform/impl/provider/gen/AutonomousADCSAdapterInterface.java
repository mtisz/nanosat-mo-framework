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
import org.ccsds.moims.mo.platform.autonomousadcs.structures.ActuatorsTelemetry;
import org.ccsds.moims.mo.platform.autonomousadcs.structures.AttitudeTelemetry;
import org.ccsds.moims.mo.platform.autonomousadcs.structures.AttitudeMode;

/**
 *
 * @author Cesar Coelho
 */
public interface AutonomousADCSAdapterInterface
{

  /**
   * Checks if the device is present and accessible.
   *
   * @return true if the device is present and available for operations.
   */
  boolean isUnitAvailable();

  /**
   * The setDesiredAttitude operation shall set a certain attitude based on the AttitudeMode
   * descriptor
   *
   * @param attitude
   * @throws IOException in case of failure on the interface
   * @throws UnsupportedOperationException if the implementation does not support the requested ADCS
   * mode
   */
  void setDesiredAttitude(AttitudeMode attitude) throws IOException, UnsupportedOperationException;

  /**
   * The unset operation shall reset the attitude mode.
   *
   * @throws IOException
   */
  void unset() throws IOException;

  /**
   * The getAttitudeTelemetry returns an object representing the attitude information provided by an
   * ADCS unit
   *
   * @return The Attitude Telemetry from the ADCS
   * @throws IOException
   */
  AttitudeTelemetry getAttitudeTelemetry() throws IOException;

  /**
   * The getActuatorsTelemetry returns an object representing the actuators information provided by
   * an ADCS unit
   *
   * @return The Attitude Telemetry from the ADCS
   * @throws IOException
   */
  ActuatorsTelemetry getActuatorsTelemetry() throws IOException;

  /**
   * Validate Attitude Descriptor
   *
   * @param attitude requested attitude mode descriptor to be validated
   * @return null if attitude descriptor is valid. Human readable string with error message if
   * validation fails.
   */
  public String validateAttitudeDescriptor(AttitudeMode attitude);

  /**
   * Get active attitude mode.
   *
   * @return Descriptor of the attitude currently set in the ADCS or NULL if none (IDLE)
   */
  public AttitudeMode getActiveAttitudeMode();

}
