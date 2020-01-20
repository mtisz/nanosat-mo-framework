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
package esa.mo.nmf.apps;

import esa.mo.helpertools.helpers.HelperAttributes;
import esa.mo.nmf.MCRegistration;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ccsds.moims.mo.mal.provider.MALInteraction;
import org.ccsds.moims.mo.mal.structures.Attribute;
import org.ccsds.moims.mo.mal.structures.Identifier;
import org.ccsds.moims.mo.mal.structures.IdentifierList;
import org.ccsds.moims.mo.mal.structures.UInteger;
import org.ccsds.moims.mo.mal.structures.UOctet;
import org.ccsds.moims.mo.mal.structures.UShort;
import org.ccsds.moims.mo.mc.action.structures.ActionDefinitionDetails;
import org.ccsds.moims.mo.mc.action.structures.ActionDefinitionDetailsList;
import org.ccsds.moims.mo.mc.structures.ArgumentDefinitionDetails;
import org.ccsds.moims.mo.mc.structures.ArgumentDefinitionDetailsList;
import org.ccsds.moims.mo.mc.structures.AttributeValueList;
import org.ccsds.moims.mo.mc.structures.ConditionalConversionList;

/**
 * Class handling acquisition of targets and the corresponding actions
 *
 * @author Kevin Otto <Kevin@KevinOtto.de>
 */
public class CameraAcquisitorSystemCameraTargetHandler
{

  //TODO set corect number as soon as stages are defined
  private static final int PHOTOGRAPH_LOCATION_STAGES = 0;

  private static final String ACTION_PHOTOGRAPH_LOCATION = "photographLocation";

  private static enum TimeModesEnum
  {
    ANY, DAYTIME, NIGHTTIME
  }

  private CameraAcquisitorSystemMCAdapter casMCAdapter;

  private static final Logger LOGGER = Logger.getLogger(
      CameraAcquisitorSystemCameraTargetHandler.class.getName());

  public CameraAcquisitorSystemCameraTargetHandler(CameraAcquisitorSystemMCAdapter casMCAdapter)
  {
    this.casMCAdapter = casMCAdapter;
  }

  /**
   * Registers all target related actions
   *
   * @param registration
   */
  static void registerActions(MCRegistration registration)
  {
    ActionDefinitionDetailsList actionDefs = new ActionDefinitionDetailsList();
    IdentifierList actionNames = new IdentifierList();

    ArgumentDefinitionDetailsList argumentsPhotographLocation = new ArgumentDefinitionDetailsList();
    {
      byte rawType = Attribute._DOUBLE_TYPE_SHORT_FORM;
      String rawUnit = "degree";
      ConditionalConversionList conditionalConversions = null;
      Byte convertedType = null;
      String convertedUnit = null;
      argumentsPhotographLocation.add(new ArgumentDefinitionDetails(
          new Identifier("targetLongitude"), null, rawType, rawUnit,
          conditionalConversions, convertedType, convertedUnit));
      argumentsPhotographLocation.add(new ArgumentDefinitionDetails(
          new Identifier("targetLatitude"), null, rawType, rawUnit,
          conditionalConversions, convertedType, convertedUnit));
      argumentsPhotographLocation.add(new ArgumentDefinitionDetails(
          new Identifier("maximumAngleToTarget"), null, rawType, rawUnit,
          conditionalConversions, convertedType, convertedUnit));
    }
    {
      byte rawType = Attribute._UINTEGER_TYPE_SHORT_FORM;
      String rawUnit = null;
      ConditionalConversionList conditionalConversions = null;
      Byte convertedType = null;
      String convertedUnit = null;
      argumentsPhotographLocation.add(new ArgumentDefinitionDetails(
          new Identifier("timeOfPhotograph"), null, rawType, rawUnit,
          conditionalConversions, convertedType, convertedUnit));
    }

    ActionDefinitionDetails actionDefTakePhotograpOfLocation = new ActionDefinitionDetails(
        "queues a new photograph target",
        new UOctet((short) 0), new UShort(PHOTOGRAPH_LOCATION_STAGES), argumentsPhotographLocation);

    actionDefs.add(actionDefTakePhotograpOfLocation);
    actionNames.add(new Identifier(ACTION_PHOTOGRAPH_LOCATION));

    registration.registerActions(actionNames, actionDefs);

  }

  public UInteger actionArrived(Identifier name, AttributeValueList attributeValues,
      Long actionInstanceObjId, boolean reportProgress, MALInteraction interaction)
  {

    LOGGER.log(Level.INFO, "number of parameters: {0}", attributeValues.size());

    if (name.getValue() != null) {
      switch (name.getValue()) {
        case (ACTION_PHOTOGRAPH_LOCATION):
          Double longitude = HelperAttributes.attribute2double(attributeValues.get(0).getValue());
          Double latitude = HelperAttributes.attribute2double(attributeValues.get(1).getValue());
          Double maxAngle = HelperAttributes.attribute2double(attributeValues.get(2).getValue());

          TimeModesEnum timeType = TimeModesEnum.ANY;

          try {
            int value = (int) ((UInteger) attributeValues.get(3).getValue()).getValue();
            timeType = TimeModesEnum.values()[value];
          } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Invalid timeOfPhotograph! \n {0}", e);
          }
          LOGGER.log(Level.INFO, "{0}:", ACTION_PHOTOGRAPH_LOCATION);
          LOGGER.log(Level.INFO, "  Longitude: {0}", longitude);
          LOGGER.log(Level.INFO, "   Latitude: {0}", latitude);
          LOGGER.log(Level.INFO, "  max Angle: {0}", maxAngle);
          LOGGER.log(Level.INFO, "  time type: {0}", timeType.name());

          return new UInteger(1);
      }
    }
    return new UInteger(0); // error code 0 - unknown error
  }
}