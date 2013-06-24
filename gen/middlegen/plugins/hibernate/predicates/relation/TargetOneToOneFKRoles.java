/*
 * 
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * - Neither the name of BEKK Consulting nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package middlegen.plugins.hibernate.predicates.relation;

import middlegen.predicates.relation.RelationshipRolePredicate;
import middlegen.Column;
import middlegen.ColumnMap;
import middlegen.RelationshipRole;
import org.apache.commons.collections.Predicate;

/**
 * Target predicate filter for one-to-one mappings attached to the foreign key
 * side of the relationship.
 *
 * @author David Channon
 * @created 26 Oct 2003
 */
public class TargetOneToOneFKRoles extends RelationshipRolePredicate {

   /**
    * @todo-javadoc Describe the field
    */
   private final static Predicate _instance = new TargetOneToOneFKRoles();


   /**
    * Describe what the method does
    *
    * @param role Describe what the parameter does
    * @return Describe the return value
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for return value
    */
   public boolean evaluate(RelationshipRole role) {
      boolean ret = false;
      if (role.getRelation().isOne2One() &&
            !role.getRelation().isMany2Many() &&
            !role.isTargetMany()) {

         ColumnMap[] targetColumnMap = role.getColumnMaps();
         // Side affect of both FK and PK on child end of association
         // results in Column map containing no values (I do think it
         // should but it doesn't so I am using this miss feature here).
         // This will have to be changed if this side affect is ever changed.
         // In v 2.x beta series its not likely to ever be changed.
         if (targetColumnMap.length <= 0) {
            return true;
         }
      }
      return ret;
   }


   /**
    * Gets the Instance attribute of the TargetOne class
    *
    * @return The Instance value
    */
   public static Predicate getInstance() {
      return _instance;
   }
}
